package com.ecommerce.catalog_service.dto.response;

import com.ecommerce.catalog_service.entity.Brand;
import com.ecommerce.catalog_service.entity.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    String name;
    String slug;
    String description;
    long basePrice;
    Boolean active;
    String status;
    String mainImage;
    Set<String> categories;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    Instant created_at;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Ho_Chi_Minh")
    Instant update_at;
}