package com.ecommerce.catalog_service.entity;

import com.ecommerce.catalog_service.util.MediaId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductMedia {
    @Id
    @MediaId
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSlug", nullable = false)
    Product product;

    String mediaUrl;
    Boolean isMain;
    Integer sortOrder;
    Instant created_at;
    Instant update_at;
}