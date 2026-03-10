package com.ecommerce.order_service.enums;

public enum OrderStatus {
    PENDING,        // Đơn hàng đang chờ xác nhận
    PROCESSING,     // Đơn hàng đang được xử lý/chuẩn bị
    PAID,           // Đã thanh toán (nếu không phải COD)
    CANCELLED,      // Đã hủy bởi khách hàng hoặc hệ thống
    CANCELLING,      // Đang chờ payment service hoàn tiền
    CANCEL_FAILURE,      // Hủy đơn hàng thất bại
    SHIPPED,        // Đã giao cho đơn vị vận chuyển
    DELIVERED,      // Đã giao thành công
    COMPLETED       // Hoàn tất chu trình (Sau khi giao thành công 1-2 ngày)
}