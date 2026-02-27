package com.ecommerce.catalog_service.dto.response;

import com.ecommerce.catalog_service.entity.Product;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductMediaResponse {
    String id;
    Product product;
    String mediaUrl;
    Boolean isMain = false;
    Integer sortOrder;
    Instant created_at;
    Instant update_at;
}