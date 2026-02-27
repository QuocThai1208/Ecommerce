package com.ecommerce.catalog_service.entity;

import com.ecommerce.catalog_service.util.AttValueId;
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
public class AttributeValue {
    @Id
    @AttValueId
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attributeId", nullable = false)
    Attribute attribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId", nullable = false)
    Brand brand;

    String value;
    String valueCode;
}