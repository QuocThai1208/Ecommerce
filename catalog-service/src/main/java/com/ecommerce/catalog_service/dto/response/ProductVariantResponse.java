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
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {
    String sku;
    String name;
    String media;
    String status;
    long priceAdjustment;
    Set<AttributeValueResponse> attributeValues;
}