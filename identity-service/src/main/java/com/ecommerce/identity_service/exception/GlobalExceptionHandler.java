package com.ecommerce.identity_service.exception;

import com.ecommerce.identity_service.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // Validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attribute = null;

        try{
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = exception.getBindingResult() // Lấy ra đối tượng chứa tất cả các lỗi Validation của request
                    .getAllErrors() // Lấy danh sách lỗi
                    .getFirst() // Lấy lỗi đầu tiên
                    .unwrap(ConstraintViolation.class); // Lấy đối tượng constraintViolation bên trong wrap

            attribute = constraintViolation.getConstraintDescriptor().getAttributes(); // Lấy map thuộc tính (key-value) được định nghĩa bên trong annotation

        }catch(IllegalArgumentException e){

        }

        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attribute) ?
                        mapAttribute(errorCode.getMessage(), attribute) :
                        errorCode.getMessage())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    ResponseEntity<ApiResponse> handlingSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception){
        ErrorCode errorCode = ErrorCode.EMAIL_EXISTED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    // Access Denied
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    private String mapAttribute(String message, Map<String, Object> attribute){
        String minValue = String.valueOf(attribute.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}