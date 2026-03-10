package com.ecommerce.order_service.dto.request;

import lombok.Builder;

import java.util.Set;

@Builder
public record InventoryReservationRequest(
        String customerLatitude,  // Tọa độ khách hàng
        String customerLongitude, // Tọa độ khách hàng
        String orderId,
        Set<ItemCheck> items
){
    public record ItemCheck(
            String productVariantId,
            Long quantity
    ){}
}