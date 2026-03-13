package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.request.NewStatusRequest;
import com.ecommerce.order_service.dto.request.OrderCancelRequest;
import com.ecommerce.order_service.dto.request.OrderCreationRequest;
import com.ecommerce.order_service.dto.request.OrderReviewRequest;
import com.ecommerce.order_service.dto.response.*;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.service.OrdersService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrdersController {
    OrdersService ordersService;

    @PostMapping
    ApiResponse<OrderCreationResponse> createOrder(@RequestBody OrderCreationRequest request) {
        return ApiResponse.<OrderCreationResponse>builder()
                .message("Create order success.")
                .result(ordersService.createOrder(request))
                .build();
    }

    @GetMapping
    ApiResponse<PageResponse<OrderSummaryResponse>> getOrdersByFilter(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        return ApiResponse.<PageResponse<OrderSummaryResponse>>builder()
                .message("Get orders by filter success.")
                .result(ordersService.getOrdersByFilter(page, size, status, userId, fromDate, toDate))
                .build();
    }

    @PostMapping("/review")
    ApiResponse<List<OrderReviewResponse>> review(@RequestBody OrderReviewRequest request) {
        return ApiResponse.<List<OrderReviewResponse>>builder()
                .message("Xem trước đơn hàng thành công.")
                .result(ordersService.review(request))
                .build();
    }

    @GetMapping("/my-orders")
    ApiResponse<PageResponse<PurchaseResponse>> getOrderByUserId(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        return ApiResponse.<PageResponse<PurchaseResponse>>builder()
                .message("Get order by user id success.")
                .result(ordersService.getOrderByUserId(page, size, status, fromDate, toDate))
                .build();
    }

    @PatchMapping("/{orderId}/status")
    ApiResponse<OrderStatusResponse> updateStaus(@PathVariable String orderId, @RequestBody NewStatusRequest request) {
        return ApiResponse.<OrderStatusResponse>builder()
                .message("Update order status success.")
                .result(ordersService.updateStatus(orderId, request))
                .build();
    }

    @GetMapping("/{orderId}")
    ApiResponse<OrderDetailResponse> getOrder(@PathVariable String orderId) {
        return ApiResponse.<OrderDetailResponse>builder()
                .message("Get detail order success.")
                .result(ordersService.getDetailOrder(orderId))
                .build();
    }

    @DeleteMapping("{orderId}")
    ApiResponse<OrderStatusResponse> cancelOrder(
            @PathVariable String orderId,
            @RequestBody OrderCancelRequest request) {
        return ApiResponse.<OrderStatusResponse>builder()
                .message("Hủy đơn hàng thành công.")
                .result(ordersService.cancelOrder(orderId, request))
                .build();
    }
}