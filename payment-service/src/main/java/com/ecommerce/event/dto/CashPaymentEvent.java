package com.ecommerce.event.dto;

import com.ecommerce.payment_service.enums.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CashPaymentEvent {
    String userId; // id khách hàng
    MethodType method;
    List<CashPaymentItemEvent> cashPaymentItemEvents;
}