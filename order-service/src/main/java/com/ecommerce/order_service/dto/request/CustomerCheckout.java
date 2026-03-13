package com.ecommerce.order_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCheckout {
    String customerLatitude;  // Tọa độ khách hàng
    String customerLongitude; // Tọa độ khách hàng
    @Builder.Default
    Set<OrderCheckout> orderCheckouts = new HashSet<>();
}