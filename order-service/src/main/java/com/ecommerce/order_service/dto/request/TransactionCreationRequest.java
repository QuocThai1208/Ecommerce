package com.ecommerce.order_service.dto.request;

import com.ecommerce.order_service.enums.MethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionCreationRequest {
    String orderId;  // id đơn hàng
    String userId; // id khách hàng
    BigDecimal amount; // số tiền giao dịch
    MethodType method; // phương thức thanh toán
    Set<ProductCheckoutRequest> products; // danh sách sản phẩm thanh toán
}