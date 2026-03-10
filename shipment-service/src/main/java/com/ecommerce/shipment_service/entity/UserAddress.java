package com.ecommerce.shipment_service.entity;

import com.ecommerce.shipment_service.util.UserAddressId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAddress {
    @Id
    @UserAddressId
    String id;
    String userId;
    String fullName; // tên người nhận hàng
    String phone; // số điện thoại nhận hàng

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardCode", nullable = false)
    MasterLocation wardCode; // địa chỉ

    String addressDetail; // địa chỉ chi tiết
    Boolean isDefault; // chọn làm mặc định
    Double latitude;  // Vĩ độ
    Double longitude; // Kinh độ
    Instant createdAt;
    Instant updateAt;
}