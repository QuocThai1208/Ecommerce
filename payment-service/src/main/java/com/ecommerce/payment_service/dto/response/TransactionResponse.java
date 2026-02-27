package com.ecommerce.payment_service.dto.response;

import com.ecommerce.payment_service.enums.MethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionResponse {
    String orderId;  // id đơn hàng
    String paymentIntentId; // id giao dịch từ cổng thanh toán
    String sessionUrl; // Url thanh toán từ stripe
}