package com.ecommerce.shipment_service.controller;

import com.ecommerce.shipment_service.dto.ApiResponse;
import com.ecommerce.shipment_service.dto.request.UserAddressRequest;
import com.ecommerce.shipment_service.dto.response.UserAddressResponse;
import com.ecommerce.shipment_service.service.UserAddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}