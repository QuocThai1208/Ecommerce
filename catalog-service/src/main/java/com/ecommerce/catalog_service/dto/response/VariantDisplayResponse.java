package com.ecommerce.catalog_service.dto.response;

import com.ecommerce.catalog_service.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantDisplayResponse {
    String sku;
    String name;
    long priceAdjustment;
    long quantityAvailable;
    String urlMedia;
    ProductStatus status;
    List<String> values;
}