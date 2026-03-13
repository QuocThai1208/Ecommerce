package com.ecommerce.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionDetailRequest {
    String orderId;  // id đơn hàng
    String brandId; // id thương hiệu
    BigDecimal amount; // số tiền giao dịch
    Set<ProductPaymentRequest> products; // danh sách sản phẩm thanh toán
}
