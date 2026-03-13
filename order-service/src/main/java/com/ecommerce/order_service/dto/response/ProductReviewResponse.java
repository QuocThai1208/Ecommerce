package com.ecommerce.order_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewResponse {
    String variantId;
    String productName;
    String variantName;
    long price;
    long quantity;
    long total;
    String image;
}