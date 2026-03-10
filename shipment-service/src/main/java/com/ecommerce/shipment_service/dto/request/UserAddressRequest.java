package com.ecommerce.shipment_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddressRequest {
    String fullName; // tên người nhận hàng
    String phone; // số điện thoại nhận hàng
    String wardCode;
    String addressDetail;
    Double latitude;  // Vĩ độ
    Double longitude; // Kinh độ
}