package com.ecommerce.shipment_service.service;

import com.ecommerce.event.dto.ShipmentCreationEvent;
import com.ecommerce.shipment_service.dto.request.CalculatorFeeRequest;
import com.ecommerce.shipment_service.dto.response.CalculatorFeeItemResponse;
import com.ecommerce.shipment_service.dto.response.CalculatorFeeResponse;
import com.ecommerce.shipment_service.dto.response.ShipmentResponse;
import com.ecommerce.shipment_service.entity.MasterLocation;
import com.ecommerce.shipment_service.enums.ShipmentStatus;
import com.ecommerce.shipment_service.exception.AppException;
import com.ecommerce.shipment_service.exception.ErrorCode;
import com.ecommerce.shipment_service.mapper.ShipmentMapper;
import com.ecommerce.shipment_service.repository.*;
import com.ecommerce.shipment_service.repository.httpClient.GhnClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class ShipmentService {
    ShipmentRepository shipmentRepository;
    CarrierRepository carrierRepository;
    UserAddressRepository addressRepository;
    ShopPickupPointRepository shopRepository;
    LocationMappingRepository locationMappingRepository;

    GhnShipmentService ghnShipmentService;

    ShipmentMapper shipmentMapper;

    GhnClient ghnClient;

    @Value("${app.ghn.api.token}")
    @NonFinal
    String ghnToken;

    public ShipmentResponse create(ShipmentCreationEvent request){
        var carrier = carrierRepository.findById(request.getCarrierId())
                .orElseThrow(() -> new AppException(ErrorCode.CARRIER_NOT_FOUND));
        var userAddress = addressRepository.findById(request.getUserAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        var shop = shopRepository.findById(request.getShopPickupPointId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_FOUND));

        var ghnResponse = ghnShipmentService.ghnCreateOrder(
                request.getOrderId(),
                userAddress,
                carrier,
                Integer.valueOf(shop.getCarrierShopId()),
                request.getCodAmount(),
                request.getItems());
        var shipment = shipmentMapper.toShipment(request);
        Map<String, Object> snapShot = new HashMap<>();
        snapShot.put("fullName", userAddress.getFullName());
        snapShot.put("phone", userAddress.getPhone());
        snapShot.put("addressDetail", userAddress.getAddressDetail());

        shipment.setTrackingNumber(ghnResponse.getData().getOrderCode());
        shipment.setCarriers(carrier);
        shipment.setShopPickupPoint(shop);
        shipment.setUserAddress(userAddress);
        shipment.setAddressSnapshot(snapShot);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setEstimatedDelivery(Instant.parse(ghnResponse.getData().getExpectedDeliveryTime()));
        shipment.setShippingFee(ghnResponse.getData().getTotalFee());
        shipment.setCodAmount(request.getCodAmount());
        var now = Instant.now();
        shipment.setCreatedAt(now);
        shipment.setUpdateAt(now);

        var shipmentResponse = shipmentMapper.toResponse(shipmentRepository.save(shipment));
        shipmentResponse.setCarrierName(shipment.getCarriers().getName());
        shipmentResponse.setShopAddress(mapAddress(shop.getWardCode(), shop.getAddressDetail()));
        shipmentResponse.setUserAddress(mapAddress(userAddress.getWardCode(), userAddress.getAddressDetail()));
        return shipmentResponse;
    }

    public CalculatorFeeResponse calculatorFee(CalculatorFeeRequest request){
        var userAddress = addressRepository.findById(request.getUserAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        Integer[] totalFeeArr = {0};
        List<CalculatorFeeItemResponse> feeItemResponse = request.getCalFeeItems().stream()
                .map(itemRequest -> {
                    var shop = shopRepository.findById(itemRequest.getShopPickupPointId())
                            .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_FOUND));

                    var carrier = carrierRepository.findById("GHN")
                            .orElseThrow(() -> new AppException(ErrorCode.CARRIER_NOT_FOUND));

                    var wardCode = locationMappingRepository.findByMasterLocationAndCarriers(
                            userAddress.getWardCode(),
                            carrier
                    ).orElseThrow(() -> new AppException(ErrorCode.WARD_CODE_NOT_FOUND));

                    var districtId = locationMappingRepository.findByMasterLocationAndCarriers(
                            userAddress.getWardCode().getParentCode(),
                            carrier
                    ).orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_FOUND));

                    var ghnFeeRequest = shipmentMapper.toGhnFeeRequest(itemRequest);
                    ghnFeeRequest.setTo_ward_code(wardCode.getCarrierValue());
                    ghnFeeRequest.setTo_district_id(Integer.valueOf(districtId.getCarrierValue()));
                    ghnFeeRequest.setService_type_id(2);

                    var ghnFeeResponse =  ghnClient.calculatorFee(
                            ghnToken,
                            Integer.valueOf(shop.getCarrierShopId()),
                            ghnFeeRequest);
                    var fee = ghnFeeResponse.getData().getTotal();
                    totalFeeArr[0] += Integer.parseInt(fee);
                    return CalculatorFeeItemResponse.builder()
                            .shopPickupPointId(itemRequest.getShopPickupPointId())
                            .fee(Integer.valueOf(fee))
                            .build();
                }).toList();
        return CalculatorFeeResponse.builder()
                .totalFee(totalFeeArr[0])
                .feeItems(feeItemResponse)
                .build();
    }

    private String mapAddress(MasterLocation ward, String addressDetail){
        var district = ward.getParentCode();
        var province = district.getParentCode();
        return String.join(", ",
                addressDetail,
                ward.getName(),
                district.getName(),
                province.getName());
    }
}