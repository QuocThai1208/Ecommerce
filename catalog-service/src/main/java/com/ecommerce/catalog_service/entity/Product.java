package com.ecommerce.catalog_service.entity;

import com.ecommerce.catalog_service.enums.ProductStatus;
import com.ecommerce.catalog_service.util.SlugUtils;
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
public class Product {
    @Id
    String slug;

    String name;
    @Column(unique = true)
    String description;
    long basePrice;

    @Builder.Default
    Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    Brand brand;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    ProductStatus status = ProductStatus.INACTIVE;

    @Enumerated(EnumType.STRING)
    ProductStatus previousStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    Set<Category> categories;
    Instant created_at;
    Instant update_at;

    // Tự động tạo id khi save
    @PrePersist
    public void generateSlug() {
        this.slug = SlugUtils.toSlug(this.name);
    }
}