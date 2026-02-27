package com.ecommerce.shipment_service.enums;

public enum ShipmentStatus {
    PENDING,
    PACKED,         // Đã đóng gói xong
    SHIPPED,        // Đã giao cho đơn vị vận chuyển
    IN_TRANSIT,     // Đang vận chuyển
    EXCEPTION,      // Có vấn đề trong quá trình vận chuyển
    DELIVERED       // Đã giao thành công
}