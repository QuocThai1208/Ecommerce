package com.ecommerce.shipment_service.entity;

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
public class ShopPickupPoint {
    @Id
    String id;
    String brandId; // id của brand từ catalog

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrierId", nullable = false)
    Carriers carriers;

    String carrierShopId; // id do đơn vị vận chuyển cung cấp
    String contactName; // Tên người đại diện của kho
    String contactPhone; // Sdt của người đại diện

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardCode", nullable = false)
    MasterLocation wardCode; // địa chỉ

    String addressDetail; // địa chỉ chi tiết
    Double latitude; // vĩ độ
    Double longitude; // kinh độ
    Instant createdAt;
    Instant updateAt;
}