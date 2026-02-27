package com.ecommerce.catalog_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;
    String slug;
    String description;
    long basePrice;
    Boolean isActive;
    String brandId;
    Set<String> categories;
}