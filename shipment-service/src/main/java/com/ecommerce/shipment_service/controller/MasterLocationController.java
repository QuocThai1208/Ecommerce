package com.ecommerce.shipment_service.controller;

import com.ecommerce.shipment_service.dto.ApiResponse;
import com.ecommerce.shipment_service.dto.request.ProvinceClientRequest;
import com.ecommerce.shipment_service.dto.response.MasterLocationResponse;
import com.ecommerce.shipment_service.dto.response.PageResponse;
import com.ecommerce.shipment_service.service.MasterLocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/master-location")
public class MasterLocationController {
    MasterLocationService masterLocationService;

    @PostMapping("/add-location-province")
    ApiResponse<String> addLocationProvince(@RequestBody ProvinceClientRequest request){
        return ApiResponse.<String>builder()
                .result(masterLocationService.addLocationProvince(request.getProvinces()))
                .build();
    }

    @PostMapping("/add-location-district")
    ApiResponse<String> addLocationDistrict(){
        return ApiResponse.<String>builder()
                .result(masterLocationService.addLocationDistrict())
                .build();
    }

    @PostMapping("/add-location-ward")
    ApiResponse<String> addLocationWard(){
        return ApiResponse.<String>builder()
                .result(masterLocationService.addLocationWard())
                .build();
    }

    @GetMapping("/provinces")
    ApiResponse<List<MasterLocationResponse>> getProvince(){
        return ApiResponse.<List<MasterLocationResponse>>builder()
                .message("Lấy danh sách tỉnh thành công.")
                .result(masterLocationService.getProvince())
                .build();
    }

    @GetMapping("/provinces/{codename}")
    ApiResponse<List<MasterLocationResponse>> getDistrict(@PathVariable("codename") String codename){
        return ApiResponse.<List<MasterLocationResponse>>builder()
                .message("Lấy danh sách tỉnh thành công.")
                .result(masterLocationService.getDistrict(codename))
                .build();
    }

    @GetMapping("/districts/{codename}")
    ApiResponse<List<MasterLocationResponse>> getWard(@PathVariable("codename") String codename){
        return ApiResponse.<List<MasterLocationResponse>>builder()
                .message("Lấy danh sách tỉnh thành công.")
                .result(masterLocationService.getWard(codename))
                .build();
    }
}