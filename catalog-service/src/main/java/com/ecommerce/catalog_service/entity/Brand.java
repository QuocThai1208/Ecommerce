package com.ecommerce.catalog_service.entity;

import com.ecommerce.catalog_service.util.BrandId;
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
public class Brand {
    @Id
    @BrandId
    String id;
    String name;
    String description;
    Instant created_at;
    Instant update_at;
}