package com.ecommerce.order_service.repository.httpClient;

import com.ecommerce.order_service.configuration.AuthenticationRequestInterceptor;
import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.request.*;
import com.ecommerce.order_service.dto.response.ItemBatchDetailResponse;
import com.ecommerce.order_service.dto.response.ProductCheckoutResponse;
import com.ecommerce.order_service.dto.response.WarehouseBestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(
        name = "inventory-service",
        configuration = {AuthenticationRequestInterceptor.class})
public interface InventoryClient {
    @PostMapping("/inventory/internal/transactions/reserve")
    ApiResponse<Boolean> reserve(@RequestBody CustomerCheckout request);

    @PostMapping("/inventory/transactions/best-warehouse")
    ApiResponse<List<WarehouseBestResponse>> findBestWarehouses(@RequestBody WarehouseBestRequest request);


}