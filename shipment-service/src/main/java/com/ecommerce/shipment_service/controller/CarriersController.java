package com.ecommerce.shipment_service.controller;

import com.ecommerce.shipment_service.dto.ApiResponse;
import com.ecommerce.shipment_service.dto.request.CarrierCreationRequest;
import com.ecommerce.shipment_service.dto.response.CarrierResponse;
import com.ecommerce.shipment_service.service.CarriersService;
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
@RequestMapping("/carriers")
public class CarriersController {
    CarriersService carriersService;

    @PostMapping
    ApiResponse<CarrierResponse> create(@RequestBody CarrierCreationRequest request){
        return ApiResponse.<CarrierResponse>builder()
                .message("Đăng ký đơn vị vận chuyển thành công.")
                .result(carriersService.create(request))
                .build();
    }
}