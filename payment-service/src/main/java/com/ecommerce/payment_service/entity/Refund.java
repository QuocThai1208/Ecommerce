package com.ecommerce.payment_service.entity;

import com.ecommerce.payment_service.enums.RefundStatus;
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
@Table(
        name = "refund",
        indexes = {
                @Index(name = "idx_refund_transactionId", columnList = "transactionId"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_refund_gatewayRefundId", columnNames = {"gatewayRefundId"})
        }
)
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionId", nullable = false)
    Transaction transaction; // Giao dịch liên quan đến hoàn tiền

    BigDecimal refundAmount; // Số tiền hoàn trả

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RefundStatus status; // Trạng thái hoàn tiền

    String reason; // Nguyên nhân hoàn tiền
    String gatewayRefundId; // Mã hoàn tiền từ cổng thanh toán
    Instant createdAt;
    Instant updateAt;
}