package com.ecommerce.order_service.dto.request;

import com.ecommerce.order_service.enums.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderReviewRequest {
    String couponCode;
    String userAddressId;
    String customerLatitude;
    String customerLongitude;
    MethodType method;
    List<BrandOrderRequest> brandOrderRequest;
}