package com.ecommerce.catalog_service.dto.request;

import com.ecommerce.catalog_service.entity.Attribute;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeValueRequest {
    String attributeId;
    String value;
}