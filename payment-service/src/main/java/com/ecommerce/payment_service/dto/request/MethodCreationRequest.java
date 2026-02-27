package com.ecommerce.payment_service.dto.request;

import com.ecommerce.payment_service.enums.MethodType;
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
public class MethodCreationRequest {
    String userId; // id người dùng sở hữu phương thức thanh toán
    String token; // token mã hóa từ dữ liệu phương thức thanh toán
    String lastFourDigits; // 4 chữ số cuối của phương thức thanh toán
    MethodType methodType; // loại phương thức thanh toán (ví dụ: CREDIT_CARD, PAYPAL, etc.)
}