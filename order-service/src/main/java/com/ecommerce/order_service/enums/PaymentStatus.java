package com.ecommerce.order_service.enums;

public enum PaymentStatus {
    PENDING,    // Đang chờ xử lý (ví dụ: chờ ngân hàng phản hồi)
    SUCCESS,    // Thanh toán thành công
    FAILED,     // Thanh toán thất bại
    REFUNDED,   // Đã hoàn tiền
    VOIDED      // Đã hủy giao dịch
}