package com.ecommerce.order_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderReviewResponse {
    String brandId;
    String brandName;
    String badge;
    long feeShip;
    List<ProductReviewResponse> products;
}