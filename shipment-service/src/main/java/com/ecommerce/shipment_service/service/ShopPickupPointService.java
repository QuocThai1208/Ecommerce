package com.ecommerce.shipment_service.service;

import com.ecommerce.event.dto.WarehouseCreationEvent;
import com.ecommerce.shipment_service.dto.request.GhnShopRequest;
import com.ecommerce.shipment_service.entity.MasterLocation;
import com.ecommerce.shipment_service.exception.AppException;
import com.ecommerce.shipment_service.exception.ErrorCode;
import com.ecommerce.shipment_service.mapper.ShopPickupPointMapper;
import com.ecommerce.shipment_service.repository.CarrierRepository;
import com.ecommerce.shipment_service.repository.LocationMappingRepository;
import com.ecommerce.shipment_service.repository.MasterLocationRepository;
import com.ecommerce.shipment_service.repository.ShopPickupPointRepository;
import com.ecommerce.shipment_service.repository.httpClient.GhnClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShopPickupPointService {
    ShopPickupPointMapper shopMapper;
    ShopPickupPointRepository shopRepository;
    MasterLocationRepository masterLocationRepository;
    LocationMappingRepository locationMappingRepository;
    CarrierRepository carrierRepository;

    GhnClient ghnClient;

    @Value("${app.ghn.api.token}")
    @NonFinal
    String ghnToken;

    @Transactional
    public void registerShop(WarehouseCreationEvent request) {
        var now = Instant.now();

        var shop = shopMapper.toShopPickupPoint(request);

        shop.setWardCode(masterLocationRepository.findById(request.getWardCode())
                .orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_FOUND)));

        var ward = shop.getWardCode();
        var district = ward.getParentCode();
        var province = district.getParentCode();
        var carrier = carrierRepository.findById("GHN")
                .orElseThrow(() -> new AppException(ErrorCode.CARRIER_NOT_FOUND));

        var wardValue = mapCarrierValue(ward);
        var districtValue = mapCarrierValue(district);
        var provinceValue = mapCarrierValue(province);

        GhnShopRequest ghnShopRequest = GhnShopRequest.builder()
                .province_id(Long.parseLong(provinceValue))
                .district_id(Long.parseLong(districtValue))
                .ward_code(wardValue)
                .name(shop.getContactName())
                .phone(shop.getContactPhone())
                .address(shop.getAddressDetail())
                .build();
        try {
            var response = ghnClient.registerShop(
                    ghnToken,
                    ghnShopRequest);

            if(response.getCode() == 200){
                shop.setCarrierShopId(String.valueOf(response.getData().getShopId()));
                shop.setCarriers(carrier);
                shop.setCreatedAt(now);
                shop.setUpdateAt(now);
                shopRepository.save(shop);
            }else{
                throw new AppException(ErrorCode.GHN_API_ERROR);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.GHN_API_ERROR);
        }
    }

    private String mapCarrierValue(MasterLocation masterLocation) {
        var carrier = carrierRepository.findById("GHN")
                .orElseThrow(() -> new AppException(ErrorCode.CARRIER_NOT_FOUND));
        var locationMapping = locationMappingRepository.findByMasterLocationAndCarriers(masterLocation, carrier)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_MAPPING_NOT_FOUND));
        return locationMapping.getCarrierValue();
    }
}