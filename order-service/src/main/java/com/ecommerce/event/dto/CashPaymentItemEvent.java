package com.ecommerce.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CashPaymentItemEvent {
    String orderId;  // id đơn hàng
    String brandId; // id thương hiệu
    BigDecimal amount; // số tiền giao dịch
}