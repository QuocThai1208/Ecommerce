package com.ecommerce.order_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String userId;
    String recipientName; // Tên người nhận
    String phone; // Số điện thoại
    String streetAddress; // Địa chỉ dường
    Double latitude; // Vĩ độ
    Double longitude; // Kinh độ
}