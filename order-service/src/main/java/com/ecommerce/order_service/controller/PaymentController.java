package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.request.PaymentCallbackRequest;
import com.ecommerce.order_service.dto.response.PaymentResponse;
import com.ecommerce.order_service.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;

    @PutMapping("/{orderId}/payment-callback" )
    ApiResponse<PaymentResponse> paymentCallback(@PathVariable String orderId, @RequestBody PaymentCallbackRequest request){
        return ApiResponse.<PaymentResponse>builder()
                .message("Cập nhật payment thành công")
                .result(paymentService.paymentCallback(orderId, request))
                .build();

    }

    @PostMapping("/{orderId}/refund")
    ApiResponse<PaymentResponse> refundPayment(@PathVariable String orderId){
        return ApiResponse.<PaymentResponse>builder()
                .message("Yêu cầu hoàn tiền thành công.")
                .result(paymentService.refundPayment(orderId))
                .build();

    }

    @GetMapping("/{orderId}/payments")
    ApiResponse<List<PaymentResponse>> getPaymentByOrderId(@PathVariable String orderId){
        return ApiResponse.<List<PaymentResponse>>builder()
                .message("Lấy danh sách payment của đơn hàng thành công.")
                .result(paymentService.getPaymentByOrderId(orderId))
                .build();

    }

    // Hủy giao dịch thanh toán đang xử lý
    @DeleteMapping("/{orderId}/payments/{paymentTransactionId}")
    ApiResponse<PaymentResponse> cancelPayment(
            @PathVariable String orderId,
            @PathVariable String paymentTransactionId){
        return ApiResponse.<PaymentResponse>builder()
                .message("Yêu cầu hủy giao dịch thành công.")
                .result(paymentService.cancelPayment(orderId, paymentTransactionId))
                .build();

    }
}