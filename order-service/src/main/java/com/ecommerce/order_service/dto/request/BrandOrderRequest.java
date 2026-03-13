package com.ecommerce.order_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandOrderRequest {
    String brandId;
    String couponCode;
    Set<ProductCheckout> productCheckouts;
}