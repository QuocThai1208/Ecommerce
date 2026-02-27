package com.ecommerce.payment_service.entity;

import com.ecommerce.payment_service.enums.MethodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(
        name = "method",
        indexes = {
                @Index(name = "idx_userId", columnList = "userId"),
                @Index(name = "idx_methodType", columnList = "methodType")
        }
)
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userId; // id người dùng sở hữu phương thức thanh toán
    String token; // token mã hóa từ dữ liệu phương thức thanh toán

    @Column(length = 4)
    String lastFourDigits; // 4 chữ số cuối của phương thức thanh toán

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MethodType methodType; // loại phương thức thanh toán (ví dụ: CREDIT_CARD, PAYPAL, etc.)

    @Builder.Default
    Boolean isDefault = false; // phương thức thanh toán mặc định

    @Builder.Default
    Boolean isActive = true; // trạng thái hoạt động của phương thức thanh toán

    Instant createdAt;
}