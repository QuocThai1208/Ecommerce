package com.ecommerce.catalog_service.repository.httpClient;

import com.ecommerce.catalog_service.configuration.AuthenticationRequestInterceptor;
import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@FeignClient(
        name = "inventory-service",
        configuration = {AuthenticationRequestInterceptor.class}
)
public interface InventoryClient {
    @GetMapping(value = "/inventory/warehouses")
    ApiResponse<List<WarehouseResponse>> getMyWarehouse(@RequestParam("brandId") String brandId);

    @PostMapping(value = "/inventory/inventories/by-variants")
    ApiResponse<List<InventoriesResponse>> getByVariantIds(@RequestBody Set<String> variantIds);

    @PostMapping(value = "/inventory/inventories/by-variants/total-available")
    ApiResponse<List<TotalAvailableResponse>> getTotalByVariantIds(@RequestBody Set<String> variantIds);
}