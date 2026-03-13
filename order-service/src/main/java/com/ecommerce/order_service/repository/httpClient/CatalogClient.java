package com.ecommerce.order_service.repository.httpClient;

import com.ecommerce.order_service.configuration.AuthenticationRequestInterceptor;
import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.request.ItemBatchDetailRequest;
import com.ecommerce.order_service.dto.response.ItemBatchDetailResponse;
import com.ecommerce.order_service.dto.response.ProductCheckoutResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(
        name = "catalog-service")
public interface CatalogClient {
    @PostMapping("/catalog/product-variants/batch-details")
    ApiResponse<List<ItemBatchDetailResponse>> getItemBatchDetails(@RequestBody ItemBatchDetailRequest request);

    @PostMapping("/catalog/product-variants/checkout")
    ApiResponse<List<ProductCheckoutResponse>> getCheckout(@RequestBody Set<String> variantIds);
}