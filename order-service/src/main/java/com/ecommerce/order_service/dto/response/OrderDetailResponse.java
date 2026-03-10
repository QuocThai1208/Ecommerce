package com.ecommerce.order_service.dto.response;

import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.entity.ShippingAddress;
import com.ecommerce.order_service.enums.MethodType;
import com.ecommerce.order_service.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    String id;
    String userId; // Id của người đặt hàng
    OrderStatus status;
    ShippingAddress shippingAddress;
    String couponCode; // Mã giảm giá đã áp dụng
    BigDecimal subtotalAmount; // Tổng giá trị hàng hóa trước thuế/ship/giảm giá.
    BigDecimal discountAmount; // Tống số tiền giảm giá được áp dụng
    BigDecimal shippingCost; // Phí vận chuyển
    BigDecimal taxAmount; // Thuế
    BigDecimal finalAmount; // Tổng số tiền cuối cùng
    MethodType method; // Phương thức thanh toán
    Instant createdAt;
    Instant updateAt;
}