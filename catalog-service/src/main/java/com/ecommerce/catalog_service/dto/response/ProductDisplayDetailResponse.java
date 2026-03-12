package com.ecommerce.catalog_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDisplayDetailResponse {
    String slug;
    String name;
    String description;
    long basePrice;
    List<String> images;
    List<ProductOptionResponse> options;
    List<VariantDisplayResponse> variants;
}