package com.ecommerce.payment_service.controller;

import com.ecommerce.payment_service.dto.ApiResponse;
import com.ecommerce.payment_service.dto.request.CashPaymentSuccessRequest;
import com.ecommerce.payment_service.dto.request.TransactionCreationRequest;
import com.ecommerce.payment_service.dto.response.TransactionResponse;
import com.ecommerce.payment_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequestMapping("/transactions")
public class TransactionController {
    TransactionService transactionService;

    @PostMapping
    ApiResponse<TransactionResponse> create(
            @RequestBody TransactionCreationRequest transactionRequest) {
        return ApiResponse.<TransactionResponse>builder()
                .message("Tạo phiên thanh toán thành công.")
                .result(transactionService.create(transactionRequest))
                .build();
    }

    @PostMapping("/cash-payment/success")
    ApiResponse<String> cashPaymentSuccess(@RequestBody CashPaymentSuccessRequest request){
        return ApiResponse.<String>builder()
                .message("Đã thu tiền.")
                .result(transactionService.cashPaymentSuccess(request))
                .build();
    }
}