package com.ecommerce.catalog_service.dto.response;

import com.ecommerce.catalog_service.entity.AttributeValue;
import com.ecommerce.catalog_service.entity.Brand;
import com.ecommerce.catalog_service.entity.Product;
import com.ecommerce.catalog_service.entity.ProductMedia;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {
    String id;
    Product product;
    String media;
    String sku;
    long priceAdjustment;
    Instant created_at;
    Instant update_at;
}