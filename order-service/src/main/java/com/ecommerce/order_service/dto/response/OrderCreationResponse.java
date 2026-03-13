package com.ecommerce.order_service.dto.response;

import com.ecommerce.order_service.entity.ShippingAddress;
import com.ecommerce.order_service.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationResponse {
    OrderStatus status;
    BigDecimal finalAmount;
    String sessionUrl;
}