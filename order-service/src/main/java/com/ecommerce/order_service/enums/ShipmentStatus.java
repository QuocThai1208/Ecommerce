package com.ecommerce.order_service.enums;

public enum ShipmentStatus {
    PACKED,         // Đã đóng gói xong
    SHIPPED,        // Đã giao cho đơn vị vận chuyển
    IN_TRANSIT,     // Đang vận chuyển
    EXCEPTION,      // Có vấn đề trong quá trình vận chuyển
    DELIVERED       // Đã giao thành công
}