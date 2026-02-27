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
    ApiResponse<PageResponse<MasterLocationResponse>> getProvince(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
        return ApiResponse.<PageResponse<MasterLocationResponse>>builder()
                .message("Lấy danh sách tỉnh thành công.")
                .result(masterLocationService.getProvince(page, size))
                .build();
    }

    @GetMapping("/provinces/{codename}")
    ApiResponse<PageResponse<MasterLocationResponse>> getDistrict(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @PathVariable("codename") String codename
    ){
        return ApiResponse.<PageResponse<MasterLocationResponse>>builder()
                .message("Lấy danh sách tỉnh thành công.")
                .result(masterLocationService.getDistrict(page, size, codename))
                .build();
    }

    @GetMapping("/districts/{codename}")
    ApiResponse<PageResponse<MasterLocationResponse>> getWard(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @PathVariable("codename") String codename
    ){
        return ApiResponse.<PageResponse<MasterLocationResponse>>builder()
                .message("Lấy danh sách tỉnh thành công.")
                .result(masterLocationService.getWard(page, size, codename))
                .build();
    }
}