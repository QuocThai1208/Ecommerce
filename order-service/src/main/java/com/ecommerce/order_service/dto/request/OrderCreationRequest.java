package com.ecommerce.order_service.dto.request;

import com.ecommerce.order_service.enums.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    ShippingAddressRequest shippingAddressRequest;
    String couponCode;
    String paymentMethod;
    String carrierId;
    String shopPickupPointId;
    String userAddressId;
    MethodType method;
    Set<OrderItemRequest> orderItemRequest;
}