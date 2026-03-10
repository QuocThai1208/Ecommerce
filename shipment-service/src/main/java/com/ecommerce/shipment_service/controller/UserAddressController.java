package com.ecommerce.shipment_service.controller;

import com.ecommerce.shipment_service.dto.ApiResponse;
import com.ecommerce.shipment_service.dto.request.UserAddressRequest;
import com.ecommerce.shipment_service.dto.response.UserAddressResponse;
import com.ecommerce.shipment_service.service.UserAddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user-address")
public class UserAddressController {
    UserAddressService userAddressService;

    @PostMapping
    ApiResponse<UserAddressResponse> create(@RequestBody UserAddressRequest request){
        return ApiResponse.<UserAddressResponse>builder()
                .message("Đăng ký địa chỉ thành công.")
                .result(userAddressService.create(request))
                .build();
    }

    @GetMapping("/is-default")
    ApiResponse<UserAddressResponse> findByUserAndDefaultTrue(){
        return ApiResponse.<UserAddressResponse>builder()
                .message("Lấy địa chỉ mặc định thành công.")
                .result(userAddressService.findByUserAndDefaultTrue())
                .build();
    }

    @PutMapping("/{addressId}/set-default")
    ApiResponse<String> setDefault(@PathVariable String addressId){
        return ApiResponse.<String>builder()
                .result(userAddressService.setIsDefault(addressId))
                .build();
    }

    @DeleteMapping("/{addressId}")
    ApiResponse<String> delete(@PathVariable String addressId){
        return ApiResponse.<String>builder()
                .result(userAddressService.delete(addressId))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserAddressResponse>> findAllByUserId(){
        return ApiResponse.<List<UserAddressResponse>>builder()
                .message("Lấy danh sách địa chỉ thành công.")
                .result(userAddressService.findAllByUserId())
                .build();
    }
}