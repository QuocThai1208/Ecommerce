package com.ecommerce.payment_service.entity;

import com.ecommerce.payment_service.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionId", nullable = false)
    Transaction transaction;

    @Column(nullable = false)
    String orderId;
    String brandId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionStatus status; // trạng thái giao dịch

    @Column(nullable = false)
    BigDecimal amount;
    Instant createdAt;
}