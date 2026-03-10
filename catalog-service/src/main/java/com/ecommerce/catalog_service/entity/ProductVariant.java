package com.ecommerce.catalog_service.entity;

import com.ecommerce.catalog_service.enums.ProductStatus;
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
    String name;
    @ManyToMany(fetch = FetchType.LAZY)
    Set<AttributeValue> attributeValues;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSlug", nullable = false)
    Product product;

    @OneToOne(fetch = FetchType.LAZY)
    ProductMedia productMedia;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    ProductStatus status = ProductStatus.INACTIVE;

    long priceAdjustment;
    Instant created_at;
    Instant update_at;
}