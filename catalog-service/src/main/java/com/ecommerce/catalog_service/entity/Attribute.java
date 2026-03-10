package com.ecommerce.catalog_service.entity;

import com.ecommerce.catalog_service.util.AttId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attribute {
    @Id
    @AttId
    String id;
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId", nullable = false)
    Brand brand;

    @OneToMany(mappedBy = "attribute")
    Set<AttributeValue> values;
}