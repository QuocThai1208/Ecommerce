package com.ecommerce.shipment_service.entity;

import com.ecommerce.shipment_service.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String orderId; // mẫ đơn hàng
    String trackingNumber; // mã vận chuyển từ đơn vị vận chuyển

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrierId", nullable = false)
    Carriers carriers; // đơn vị vân chuyển

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickupPointId", nullable = false)
    ShopPickupPoint shopPickupPoint; // địa chỉ lấy hàng

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userAddressId", nullable = false)
    UserAddress userAddress; // địa chỉ nhận hàng

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "address_snapshot", columnDefinition = "jsonb")
    Map<String, Object> addressSnapshot; // địa chỉ nhận hàng khi tạo shipment

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ShipmentStatus status;

    Instant estimatedDelivery; // thời gian giao dự kiến
    BigDecimal shippingFee; // phí ship
    BigDecimal codAmount; // số tiền thu hộ

    Instant createdAt;
    Instant updateAt;
}