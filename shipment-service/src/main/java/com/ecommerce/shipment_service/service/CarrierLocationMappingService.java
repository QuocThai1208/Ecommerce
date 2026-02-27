package com.ecommerce.shipment_service.service;

import com.ecommerce.shipment_service.dto.request.WardGhnRequest;
import com.ecommerce.shipment_service.entity.CarrierLocationMapping;
import com.ecommerce.shipment_service.enums.LocationType;
import com.ecommerce.shipment_service.exception.AppException;
import com.ecommerce.shipment_service.exception.ErrorCode;
import com.ecommerce.shipment_service.repository.CarrierRepository;
import com.ecommerce.shipment_service.repository.LocationMappingRepository;
import com.ecommerce.shipment_service.repository.MasterLocationRepository;
import com.ecommerce.shipment_service.repository.httpClient.GhnClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class CarrierLocationMappingService {
    GhnClient ghnClient;

    MasterLocationRepository masterLocationRepository;
    CarrierRepository carrierRepository;
    LocationMappingRepository locationMappingRepository;

    @Value("${app.ghn.api.token}")
    @NonFinal
    String ghnToken;

    public void mapGhnProvince(){
        var response = ghnClient.getProvince(ghnToken);
        var data = response.getData();
        var ghn = carrierRepository.findById("GHN")
                .orElseThrow(() -> new AppException(ErrorCode.CARRIER_NOT_FOUND));
        var province = masterLocationRepository.findAllByType(LocationType.PROVINCE);

        List<CarrierLocationMapping> locationMappings = province.stream()
                .map(p -> data.stream()
                        .filter(ghnPro -> ghnPro.getNameExtension() != null &&
                                ghnPro.getNameExtension().stream().anyMatch(ext -> ext.equalsIgnoreCase(p.getName())))
                        .findFirst()
                        .map(ghnPro -> {
                            return CarrierLocationMapping.builder()
                                    .masterLocation(p)
                                    .carriers(ghn)
                                    .carrierValue(ghnPro.getProvinceID().toString())
                                    .build();
                        })
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .toList();
        locationMappingRepository.saveAll(locationMappings);
    }

    public void mapGhnDistrict(){
        var response = ghnClient.getDistrict(ghnToken);
        var data = response.getData();
        var ghn = carrierRepository.findById("GHN")
                .orElseThrow(() -> new AppException(ErrorCode.CARRIER_NOT_FOUND));
        var districts = masterLocationRepository.findAllByType(LocationType.DISTRICT);

        List<CarrierLocationMapping> locationMappings = districts.stream()
                .map(d -> data.stream()
                        .filter(ghnPro -> ghnPro.getNameExtension() != null &&
                                ghnPro.getNameExtension().stream().anyMatch(ext -> ext.equalsIgnoreCase(d.getName())))
                        .findFirst()
                        .map(ghnPro -> {
                            return CarrierLocationMapping.builder()
                                    .masterLocation(d)
                                    .carriers(ghn)
                                    .carrierValue(ghnPro.getDistrictID().toString())
                                    .build();
                        })
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .toList();
        locationMappingRepository.saveAll(locationMappings);
    }

    @Transactional
    public void mapGhnWard(){
        var ghn = carrierRepository.findById("GHN")
                .orElseThrow(() -> new AppException(ErrorCode.CARRIER_NOT_FOUND));
        var districts = masterLocationRepository.findAllByType(LocationType.DISTRICT);
        var districtGhn = districts.stream()
                .map(d -> locationMappingRepository.findByMasterLocation(d)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
        districtGhn.forEach(dg -> {
            var response = ghnClient.getWard(
                    ghnToken,
                    WardGhnRequest.builder()
                            .district_id(Long.valueOf(dg.getCarrierValue()))
                            .build());
            var data = response.getData();
            var ward = masterLocationRepository.findAllByParentCode(dg.getMasterLocation());

            List<CarrierLocationMapping> locationMappings = ward.stream()
                    .map(w -> data.stream()
                            .filter(ghnPro -> ghnPro.getNameExtension() != null &&
                                    ghnPro.getNameExtension().stream().anyMatch(ext -> ext.equalsIgnoreCase(w.getName())))
                            .findFirst()
                            .map(ghnPro -> {
                                return CarrierLocationMapping.builder()
                                        .masterLocation(w)
                                        .carriers(ghn)
                                        .carrierValue(ghnPro.getWardCode())
                                        .build();
                            })
                            .orElse(null)
                    )
                    .filter(Objects::nonNull)
                    .toList();
            locationMappingRepository.saveAll(locationMappings);
        });
    }
}