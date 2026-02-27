package com.ecommerce.shipment_service.exception;

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
    PROVINCE_NOT_FOUND(1022, "Không tìm thấy thông tin tỉnh.", HttpStatus.BAD_REQUEST),
    DISTRICT_NOT_FOUND(1023, "Không tìm thấy thông tin huyện.", HttpStatus.BAD_REQUEST),
    CARRIER_NOT_FOUND(1024, "Không tìm thấy đơn vị vân chuyển.", HttpStatus.BAD_REQUEST),
    LOCATION_MAPPING_NOT_FOUND(1025, "Không tìm thấy giá trị của đơn vị giao hàng.", HttpStatus.BAD_REQUEST),
    GHN_API_ERROR(1026, "Không thể gọi api của giao hàng nhanh.", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(1027, "Không tìm thấy địa chỉ khách hàng.", HttpStatus.BAD_REQUEST),
    WARD_CODE_NOT_FOUND(1028, "Không tìm thấy code của xã.", HttpStatus.BAD_REQUEST),
    DISTRICT_CODE_NOT_FOUND(1029, "Không tìm thấy code của huyện.", HttpStatus.BAD_REQUEST),
    SHOP_NOT_FOUND(1030, "Không tìm thấy shop.", HttpStatus.BAD_REQUEST),
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