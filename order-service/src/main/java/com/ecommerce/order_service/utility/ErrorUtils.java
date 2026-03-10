package com.ecommerce.order_service.utility;

import com.ecommerce.order_service.exception.RemoteError;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorUtils {
    static ObjectMapper MAPPER = new ObjectMapper();

    public static Optional<RemoteError> parseRemoteError(String responseBody) {
        try {
            // tìm và cắt chuỗi json
            int jsonStart = responseBody.indexOf("[{");
            if(jsonStart == -1) return Optional.empty();
            // cắt chuỗi từ vị trí jsonstart cho đến hết chuỗi
            String jsonString = responseBody.substring(jsonStart);
            // loại bỏ []
            if(jsonString.startsWith("[") && jsonString.endsWith("]")) {
                jsonString = jsonString.substring(1, jsonString.length() - 1);
            }

            // chuyển chuỗi json thành object RemoteError
            RemoteError remoteError = MAPPER.readValue(jsonString, RemoteError.class);
            return Optional.of(remoteError);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}