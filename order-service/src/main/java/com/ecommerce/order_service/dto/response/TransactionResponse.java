package com.ecommerce.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionResponse {
    String orderId;  // id đơn hàng
    String gatewayTransactionId; // id giao dịch từ cổng thanh toán
    String sessionUrl; // Url thanh toán từ stripe
}