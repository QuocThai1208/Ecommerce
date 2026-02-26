package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.ApiResponse;
import com.ecommerce.inventory_service.dto.request.CustomerCheckout;
import com.ecommerce.inventory_service.service.InventoryTransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalController {
    InventoryTransactionService transactionService;

    @PostMapping("/transactions/reserve")
    ApiResponse<Boolean> reserve(@RequestBody CustomerCheckout request) {
        return ApiResponse.<Boolean>builder()
                .message("Đặt hàng thành công.")
                .result(transactionService.reserveMultipleOrder(request))
                .build();
    }
}