package com.ecommerce.shipment_service.controller;


import com.ecommerce.shipment_service.dto.ApiResponse;
import com.ecommerce.shipment_service.dto.response.ProvinceGhnResponse;
import com.ecommerce.shipment_service.service.CarrierLocationMappingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/location-mapping")
public class CarrierLocationMappingController {
    CarrierLocationMappingService locationMappingService;

    @GetMapping("/gnh/province")
    ApiResponse<Void> mapGhnProvince(){
        locationMappingService.mapGhnProvince();
        return ApiResponse.<Void>builder()
                .message("Map thông tin tỉnh qua ghn thành công.")
                .build();
    }

    @GetMapping("/gnh/district")
    ApiResponse<Void> mapGhnDistrict(){
        locationMappingService.mapGhnDistrict();
        return ApiResponse.<Void>builder()
                .message("Map thông tin tỉnh qua ghn thành công.")
                .build();
    }

    @GetMapping("/gnh/ward")
    ApiResponse<Void> mapGhnWard(){
        locationMappingService.mapGhnWard();
        return ApiResponse.<Void>builder()
                .message("Map thông tin tỉnh qua ghn thành công.")
                .build();
    }
}