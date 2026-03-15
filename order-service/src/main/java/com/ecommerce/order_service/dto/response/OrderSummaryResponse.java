package com.ecommerce.order_service.dto.response;

import com.ecommerce.order_service.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderSummaryResponse {
    String id;
    OrderStatus status;
    BigDecimal finalAmount; // Tổng số tiền cuối cùng
    String paymentMethod; // Phương thức thanh toán
    String brandName;
    Instant createdAt;
}