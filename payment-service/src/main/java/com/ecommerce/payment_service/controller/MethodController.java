package com.ecommerce.payment_service.controller;

import com.ecommerce.payment_service.dto.ApiResponse;
import com.ecommerce.payment_service.dto.request.MethodCreationRequest;
import com.ecommerce.payment_service.dto.response.MethodResponse;
import com.ecommerce.payment_service.service.MethodService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/methods")
public class MethodController {
    MethodService methodService;

    @PostMapping
    ApiResponse<MethodResponse> create(@RequestBody MethodCreationRequest request){
        return ApiResponse.<MethodResponse>builder()
                .message("Tạo phương thức thanh toán thành công.")
                .result(methodService.create(request))
                .build();
    }

    @PatchMapping("/{methodId}/default")
    ApiResponse<MethodResponse> choiceIsDefault(@PathVariable String methodId) {
        return ApiResponse.<MethodResponse>builder()
                .message("Chọn phương thức thanh toán mặc định thành công.")
                .result(methodService.choiceIsDefault(methodId))
                .build();
    }

    @PatchMapping("/{methodId}/inactive")
    ApiResponse<String> inactive(@PathVariable String methodId) {
        return ApiResponse.<String>builder()
                .message("Vô hiệu hóa phương thức thanh toán.")
                .result(methodService.inactive(methodId))
                .build();
    }

    @GetMapping("/{methodId}")
    ApiResponse<MethodResponse> getById(@PathVariable String methodId){
        return ApiResponse.<MethodResponse>builder()
                .message("Lấy thông tin phương thức thanh toán thành công.")
                .result(methodService.getMethod(methodId))
                .build();
    }

    @GetMapping("/my-methods")
    ApiResponse<List<MethodResponse>> getMethods(){
        return ApiResponse.<List<MethodResponse>>builder()
                .message("Lấy danh sách phương thức thanh toán thành công.")
                .result(methodService.getMyMethods())
                .build();
    }
}