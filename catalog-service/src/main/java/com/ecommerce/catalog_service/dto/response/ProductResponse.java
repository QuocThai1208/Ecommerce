package com.ecommerce.catalog_service.dto.response;

import com.ecommerce.catalog_service.entity.Brand;
import com.ecommerce.catalog_service.entity.Category;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String slug;
    String description;
    long basePrice;
    Boolean active;
    Brand brand;
    Set<Category> categories;
    Instant created_at;
    Instant update_at;
}