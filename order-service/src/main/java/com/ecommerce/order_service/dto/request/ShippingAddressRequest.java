package com.ecommerce.order_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingAddressRequest {
    String recipientName; // Tên người nhận
    String phone; // Số điện thoại
    String streetAddress; // Địa chỉ dường
    String ward; // Phường
    String district; // Huyện
    String province; // Tỉnh
    Double latitude; // Vĩ độ
    Double longitude; // Kinh độ
}