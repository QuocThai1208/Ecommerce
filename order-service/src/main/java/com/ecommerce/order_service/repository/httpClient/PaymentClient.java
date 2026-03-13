package com.ecommerce.order_service.repository.httpClient;

import com.ecommerce.order_service.configuration.AuthenticationRequestInterceptor;
import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.request.TransactionCreationRequest;
import com.ecommerce.order_service.dto.response.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service")
public interface PaymentClient {
    @PostMapping("/payment/transactions")
    ApiResponse<TransactionResponse> createTransaction(@RequestBody TransactionCreationRequest request);
}