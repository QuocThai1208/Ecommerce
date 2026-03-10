package com.ecommerce.catalog_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    String slug;
    String name;
    String description;
    Set<String> categories;
    Set<String> images;
    List<ProductVariantResponse> variants;
    List<WarehouseResponse> warehouses;
    List<InventoriesResponse> inventories;
}