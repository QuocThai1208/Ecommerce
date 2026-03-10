package com.ecommerce.order_service.entity;

import com.ecommerce.order_service.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderShipments {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    Orders order;

    String trackingNumber; // Mã theo dõi vận chuyển (tracking number) của đối tác vận chuyển.
    String carrierName; // Tên đơn vị vận chuyển (ví dụ: VNPost, Giao Hàng Nhanh).

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ShipmentStatus shipmentStatus;

    Instant shipmentDate; // Ngày gửi hàng đi
    Date estimatedDeliveryDate; // Ngày dự kiến giao hàng
    Instant createdAt;

}