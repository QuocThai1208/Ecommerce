package com.ecommerce.order_service.entity;

import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    Orders order;

    String paymentTransactionId; // Id giao dịch được trả về từ payment service hoặc cổng thanh toán

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionType transactionType;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    PaymentStatus status = PaymentStatus.PENDING;

    String originalTransactionId;

    String paymentGateway; // Cổng thanh toán được sử dụng
    Instant createdAt;
}