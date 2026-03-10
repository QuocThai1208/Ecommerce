package com.ecommerce.order_service.repository.httpClient;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.request.InventoryReservationRequest;
import com.ecommerce.order_service.dto.request.InventoryTransactionRequest;
import com.ecommerce.order_service.dto.request.ItemBatchDetailRequest;
import com.ecommerce.order_service.dto.response.ItemBatchDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(
        name = "inventory-service")
public interface InventoryClient {
    @PostMapping("/inventory/internal/transactions/reserve")
    ApiResponse<Boolean> reserve(@RequestBody InventoryReservationRequest request);
}