package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.response.ItemResponse;
import com.ecommerce.order_service.dto.response.PageResponse;
import com.ecommerce.order_service.service.OrderItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    OrderItemService itemService;

    @GetMapping("/{orderId}/items" )
    ApiResponse<PageResponse<ItemResponse>> getOrderItems(
            @PathVariable String orderId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int size){
        return ApiResponse.<PageResponse<ItemResponse>>builder()
                .message("Lấy danh sách item trong order thành công")
                .result(itemService.getOrderItems(orderId, page, size))
                .build();

    }
}