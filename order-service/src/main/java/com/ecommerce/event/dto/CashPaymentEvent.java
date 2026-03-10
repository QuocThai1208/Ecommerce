package com.ecommerce.event.dto;

import com.ecommerce.order_service.enums.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CashPaymentEvent {
    String orderId;  // id đơn hàng
    String userId; // id khách hàng
    BigDecimal amount; // số tiền giao dịch
    MethodType method;
}