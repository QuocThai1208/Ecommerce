package com.ecommerce.event.dto;

import com.ecommerce.payment_service.enums.PaymentStatus;
import com.ecommerce.payment_service.enums.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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