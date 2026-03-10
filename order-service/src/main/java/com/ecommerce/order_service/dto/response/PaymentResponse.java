package com.ecommerce.order_service.dto.response;

import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String id;
    String paymentTransactionId;
    TransactionType transactionType;
    BigDecimal amount;
    PaymentStatus status;
    String paymentGateway;
    Instant createdAt;
}