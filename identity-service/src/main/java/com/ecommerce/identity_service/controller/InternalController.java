package com.ecommerce.identity_service.controller;

import com.ecommerce.identity_service.dto.ApiResponse;
import com.ecommerce.identity_service.dto.response.EmailResponse;
import com.ecommerce.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalController {
    UserService userService;

    @GetMapping("/my-email")
    ApiResponse<EmailResponse> getMyEmail(){
        return ApiResponse.<EmailResponse>builder()
                .message("Lấy thông tin email thành công.")
                .result(userService.getMyEmail())
                .build();
    }
}
