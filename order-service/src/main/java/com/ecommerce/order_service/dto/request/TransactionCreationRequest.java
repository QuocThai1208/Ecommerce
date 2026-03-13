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
    String userId; // id khách hàng
    MethodType method; // phương thức thanh toán
    List<TransactionDetailRequest> transactionDetailRequests;
}