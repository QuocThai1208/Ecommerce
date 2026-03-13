package com.ecommerce.order_service.repository.httpClient;

import com.ecommerce.order_service.configuration.AuthenticationRequestInterceptor;
import com.ecommerce.order_service.dto.request.CalculatorFeeRequest;
import com.ecommerce.order_service.dto.response.CalculatorFeeResponse;
import com.ecommerce.order_service.dto.response.GhnFeeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "shipment-service")
public interface ShipmentClient {
    @PostMapping("/shipment/shipments/calculator-fee")
    CalculatorFeeResponse calculatorFee(@RequestBody CalculatorFeeRequest request);
}