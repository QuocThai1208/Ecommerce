package com.ecommerce.order_service.dto.request;

import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
    String paymentTransactionId;
    TransactionType transactionType;
    BigDecimal amount;
    PaymentStatus status;
    String paymentGateway;
}