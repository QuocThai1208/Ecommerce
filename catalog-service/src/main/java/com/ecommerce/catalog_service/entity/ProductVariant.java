package com.ecommerce.catalog_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariant {
    @Id
    String sku;

    @ManyToMany(fetch = FetchType.LAZY)
    Set<AttributeValue> attributeValues;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    Product product;

    @OneToOne(fetch = FetchType.LAZY)
    ProductMedia productMedia;

    long priceAdjustment;
    Instant created_at;
    Instant update_at;
}