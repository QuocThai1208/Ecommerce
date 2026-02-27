package com.ecommerce.shipment_service.service;

import com.ecommerce.event.dto.GhnItem;
import com.ecommerce.shipment_service.dto.request.GhnCreateOrderRequest;
import com.ecommerce.shipment_service.dto.response.GhnCreateOrderResponse;
import com.ecommerce.shipment_service.entity.Carriers;
import com.ecommerce.shipment_service.entity.UserAddress;
import com.ecommerce.shipment_service.exception.AppException;
import com.ecommerce.shipment_service.exception.ErrorCode;
import com.ecommerce.shipment_service.repository.LocationMappingRepository;
import com.ecommerce.shipment_service.repository.UserAddressRepository;
import com.ecommerce.shipment_service.repository.httpClient.GhnClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GhnShipmentService {
    GhnClient ghnClient;

    UserAddressRepository addressRepository;
    LocationMappingRepository locationMappingRepository;

    @Value("${app.ghn.api.token}")
    @NonFinal
    String ghnToken;

    public GhnCreateOrderResponse ghnCreateOrder(
            String orderId,
            UserAddress userAddress,
            Carriers carriers,
            Integer shopId,
            BigDecimal codAmount,
            List<GhnItem> items
    ) {

        var toWardCode = locationMappingRepository.findByMasterLocationAndCarriers(
                        userAddress.getWareCode(),
                        carriers)
                .orElseThrow(() -> new AppException(ErrorCode.WARD_CODE_NOT_FOUND));

        var toDistrictId = locationMappingRepository.findByMasterLocationAndCarriers(
                        userAddress.getWareCode().getParentCode(),
                        carriers)
                .orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_FOUND));

        GhnCreateOrderRequest request = GhnCreateOrderRequest.builder()
                .client_order_code(orderId)
                .to_name(userAddress.getFullName())
                .to_phone(userAddress.getPhone())
                .to_address(userAddress.getAddressDetail())
                .to_ward_code(toWardCode.getCarrierValue())
                .to_district_id(Integer.valueOf(toDistrictId.getCarrierValue()))
                .cod_amount(codAmount.intValue())
                .payment_type_id(2)
                .required_note("CHOXEMHANGKHONGTHU")
                .weight(1000)
                .service_type_id(2)
                .items(items)
                .build();
        return ghnClient.createOrder(ghnToken, shopId, request);
    }

}