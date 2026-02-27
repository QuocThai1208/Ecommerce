package com.ecommerce.payment_service.enums;

public enum RefundStatus {
    // Yêu cầu đã được hệ thống ghi nhận và đang chờ gửi đến Gateway.
    REQUESTED,

    // Yêu cầu đã được gửi đến Gateway và đang trong quá trình xử lý.
    PROCESSING,

    // Hoàn tiền đã được Gateway xác nhận thành công và tiền đã được trả lại cho khách hàng.
    COMPLETED,

    // Yêu cầu hoàn tiền đã bị Gateway từ chối hoặc thất bại do lỗi kỹ thuật.
    FAILED,

    // Yêu cầu hoàn tiền bị hủy bởi hệ thống hoặc người dùng trước khi Gateway xử lý.
    CANCELLED
}