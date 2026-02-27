package com.ecommerce.catalog_service.dto.response;

import com.ecommerce.catalog_service.entity.Attribute;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeValueResponse {
    String id;
    AttributeResponse attribute;
    String value;
    String valueCode;
    String brandName;
}