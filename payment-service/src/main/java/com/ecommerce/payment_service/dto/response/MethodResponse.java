package com.ecommerce.payment_service.dto.response;

import com.ecommerce.payment_service.enums.MethodType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class MethodResponse {
    String id;
    String userId; // id người dùng sở hữu phương thức thanh toán
    String token; // token mã hóa từ dữ liệu phương thức thanh toán
    String lastFourDigits; // 4 chữ số cuối của phương thức thanh toán
    MethodType methodType; // loại phương thức thanh toán (ví dụ: CREDIT_CARD, PAYPAL, etc.)
    Boolean isDefault = false; // phương thức thanh toán mặc định
    Boolean isActive = true; // trạng thái hoạt động của phương thức thanh toán
    Instant createdAt;
}