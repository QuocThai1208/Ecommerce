package com.ecommerce.file_service.expention;

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
    PROFILE_NOT_FOUND(1013, "Profile not found.", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAIL(1014, "File upload fail.", HttpStatus.BAD_REQUEST),
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