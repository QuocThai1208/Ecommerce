package com.ecommerce.payment_service.entity;

import com.ecommerce.payment_service.enums.MethodType;
import com.ecommerce.payment_service.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(
        name = "transaction",
        indexes = {
                // tạo index cho các cột có thẻ trùng nhau
                @Index(name = "idx_transaction_user_id", columnList = "userId")
        },
        uniqueConstraints = {
                // tạo index cho các cột unique
                @UniqueConstraint(name = "uc_transaction_gatewayTransactionId", columnNames = {"gatewayTransactionId"})
        }
)
public class Transaction {
    @Id
    String id;
    String userId; // id khách hàng
    BigDecimal totalAmount; // số tiền giao dịch

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    List<TransactionDetail> details;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MethodType method; // phương thức thanh toán

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionStatus status; // trạng thái giao dịch
    String paymentIntentId; // mã hoàn tiền

    @Builder.Default
    Boolean isCapture = false; // trạng thái giao dịch đã được ghi nhận hay chưa
    Instant createdAt;
    Instant updateAt;
}