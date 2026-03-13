package com.ecommerce.order_service.dto.response;

import com.ecommerce.order_service.enums.MethodType;
import com.ecommerce.order_service.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseResponse {
    String id;
    OrderStatus status;
    BigDecimal finalAmount; // Tổng số tiền cuối cùng
    MethodType method; // Phương thức thanh toán
    Instant createdAt;
    String brandName;
    List<PurchaseItemResponse> purchaseItems;
}