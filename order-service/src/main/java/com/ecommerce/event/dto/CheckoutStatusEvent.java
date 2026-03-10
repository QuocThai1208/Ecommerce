package com.ecommerce.event.dto;

import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutStatusEvent {
    String orderId;
    String paymentTransactionId;
    TransactionType transactionType;
    PaymentStatus status;
}