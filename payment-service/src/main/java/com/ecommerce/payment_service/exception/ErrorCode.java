package com.ecommerce.payment_service.exception;

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
    VALUE_PARAM_INVALID(1015, "Giá trị của {paramName} không hơp lệ", HttpStatus.BAD_REQUEST),
    DATE_PARAM_INVALID(1021, "Date không hợp lệ, date phải có dạng yyyy-mm-dd.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(5000, "Lỗi hệ thống inventory service.", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_FOUND(1029, "Không tìm thấy phương thức thanh toán.", HttpStatus.BAD_REQUEST),
    TRANSACTION_NOT_FOUND(1008, "Không tìm thấy phiên thanh toán.", HttpStatus.BAD_REQUEST),
    TRANSACTION_DETAIL_NOT_FOUND(1009, "Không tìm thấy phiên thanh toán của đơn hàng.", HttpStatus.BAD_REQUEST),
    CASH_ORDER_ERROR(1010, "Thu tiênf đơn hàng không hợp lệ.", HttpStatus.BAD_REQUEST),
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