package com.ecommerce.order_service.entity;

import com.ecommerce.order_service.enums.MethodType;
import com.ecommerce.order_service.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders {
    @Id
    String id;

    String userId; // Id của người đặt hàng

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingAddressId", nullable = false)
    ShippingAddress shippingAddress;

    String couponCode; // Mã giảm giá đã áp dụng
    BigDecimal subtotalAmount; // Tổng giá trị hàng hóa trước thuế/ship/giảm giá.

    @Builder.Default
    BigDecimal discountAmount = BigDecimal.ZERO; // Tống số tiền giảm giá được áp dụng

    @Builder.Default
    BigDecimal shippingCost = BigDecimal.ZERO; // Phí vận chuyển

    @Builder.Default
    BigDecimal taxAmount = BigDecimal.ZERO; // Thuế
    BigDecimal finalAmount; // Tổng số tiền cuối cùng
    MethodType method; // Phương thức thanh toán
    Instant createdAt;
    Instant updateAt;

}