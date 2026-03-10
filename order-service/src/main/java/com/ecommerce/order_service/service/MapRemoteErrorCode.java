package com.ecommerce.order_service.service;

import com.ecommerce.order_service.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapRemoteErrorCode {
    public ErrorCode mapRemoteErrorCode(int codeValue){
        return switch (codeValue){
            case 1026 -> ErrorCode.INSUFFICIENT_QUANTITY;
            case 1027 -> ErrorCode.FILTER_SUFFICIENT_INVENTORIES_ERROR;
            default -> ErrorCode.INTERNAL_SERVER_ERROR;
        };
    }
}