package com.ecommerce.shipment_service.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddressResponse {
    String id;
    String userId;
    String fullName; // tên người nhận hàng
    String phone; // số điện thoại nhận hàng
    String address; // địa chỉ chi tiết
    Boolean isDefault; // chọn làm mặc định
    Instant createdAt;
    Instant updateAt;
}