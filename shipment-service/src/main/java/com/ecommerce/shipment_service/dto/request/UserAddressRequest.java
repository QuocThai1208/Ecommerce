package com.ecommerce.shipment_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddressRequest {
    String userId;
    String fullName; // tên người nhận hàng
    String phone; // số điện thoại nhận hàng
    String wareCode;
    String addressDetail;
}