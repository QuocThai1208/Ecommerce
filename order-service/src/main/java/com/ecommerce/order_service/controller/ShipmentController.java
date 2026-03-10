package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.request.ShipmentRequest;
import com.ecommerce.order_service.dto.request.ShipmentUpdateStatusRequest;
import com.ecommerce.order_service.dto.response.ShipmentResponse;
import com.ecommerce.order_service.service.ShipmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class ShipmentController {
    ShipmentService shipmentService;

    // Được gọi từ shipment service sau khi đã đóng gói hàng xong
    @PostMapping("/{orderId}/shipments")
    ApiResponse<ShipmentResponse> create(@PathVariable String orderId, @RequestBody ShipmentRequest request) {
        return ApiResponse.<ShipmentResponse>builder()
                .message("Tạp lô hàng và cập nhật trạng thái của đơn hành thành công.")
                .result(shipmentService.create(orderId, request))
                .build();
    }

    // Lấy thông tin lô hàng theo orderId
    @GetMapping("/{orderId}/shipments")
    ApiResponse<List<ShipmentResponse>> getByOrderId(@PathVariable String orderId) {
        return ApiResponse.<List<ShipmentResponse>>builder()
                .message("Lấy thông tin lô hàng thành công.")
                .result(shipmentService.getByOrderId(orderId))
                .build();
    }

    // Lấy thông tin lô hàng theo shipmentId
    @GetMapping("/{orderId}/shipments/{shipmentId}")
    ApiResponse<ShipmentResponse> getById(@PathVariable String orderId, @PathVariable String shipmentId) {
        return ApiResponse.<ShipmentResponse>builder()
                .message("Lấy thông tin lô hàng thành công.")
                .result(shipmentService.getById(shipmentId, orderId))
                .build();
    }

    // cập nhật trạng thái vận chuyển
    @PatchMapping("/{orderId}/shipments/{shipmentId}/status")
    ApiResponse<ShipmentResponse> updateStatus(@PathVariable String orderId,
                                               @PathVariable String shipmentId,
                                               @RequestBody ShipmentUpdateStatusRequest request) {
        return ApiResponse.<ShipmentResponse>builder()
                .message("Cập nhật trạng thấi lô hành và đơn hàng thành công.")
                .result(shipmentService.updateStatus(orderId, shipmentId, request))
                .build();
    }

}