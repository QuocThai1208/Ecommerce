package com.ecommerce.catalog_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductItemCheckoutResponse {
    String variantId;
    String productName;
    String variantName;
    long unitPriceSnapshot;
    String image;
}