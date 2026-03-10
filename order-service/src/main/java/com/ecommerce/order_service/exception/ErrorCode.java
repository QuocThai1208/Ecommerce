package com.ecommerce.order_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(1001, "Unauthenticated.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "You do not have permission.", HttpStatus.FORBIDDEN),
    USER_EXISTED(1003, "User existed.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found.", HttpStatus.NOT_FOUND),
    INVALID_KEY(1005, "Invalid message key.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "User not existed.", HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(1007, "Email existed.", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1008, "Role not existed.", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1008, "Permission not existed.", HttpStatus.BAD_REQUEST),
    USERNAME_VALID(1009, "User name must be at least 3 characters.", HttpStatus.BAD_REQUEST),
    PASSWORD_VALID(1010, "Password must be at least 8 characters.", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1011, "Your age at be least {min}.", HttpStatus.BAD_REQUEST),
    CANNOT_SEND_EMAIL(1012, "Can not send email.", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(1013, "Order not existed.", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_CANCEL(1014, "Order cannot be cancelled.", HttpStatus.BAD_REQUEST),
    VALUE_PARAM_INVALID(1015, "Giá trị của {paramName} không hơp lệ", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND(1016, "Thông tin thanh toán không tìm thấy", HttpStatus.BAD_REQUEST),
    UPDATE_ORDER_STATUS_ERROR(1018, "Lỗi cập nhật status của order", HttpStatus.BAD_REQUEST),
    SHIPMENT_ALREADY_EXISTED(1019, "Lô hàng đã được vận chuyển.", HttpStatus.BAD_REQUEST),
    SHIPMENT_NOT_FOUND(1020, "Không tìm thấy lô hàng.", HttpStatus.BAD_REQUEST),
    DATE_PARAM_INVALID(1021, "Date không hợp lệ, date phải có dạng yyyy-mm-dd.", HttpStatus.BAD_REQUEST),
    CATALOG_SERVICE_ERROR(1022, "Lỗi khi gọi catalog service.", HttpStatus.BAD_REQUEST),
    INVENTORY_SERVICE_ERROR(1023, "Lỗi khi gọi inventory service.", HttpStatus.BAD_REQUEST),
    RESERVE_ERROR(1023, "Số lượng sản phẩm tồn kho không đủ.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_QUANTITY(1026, "Số lượng đặt hàng trong kho không đủ.", HttpStatus.BAD_REQUEST),
    FILTER_SUFFICIENT_INVENTORIES_ERROR(1027, "Không có kho nào đủ số lượng.", HttpStatus.BAD_REQUEST),
    ACCESS_ERROR(1028, "Kết nối thất bại khi gọi service.", HttpStatus.GATEWAY_TIMEOUT),
    ORDER_BEING_PROCESSED(1029, "Đơn hàng đang được xử lý.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(5000, "Lỗi hệ thống inventory service.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}