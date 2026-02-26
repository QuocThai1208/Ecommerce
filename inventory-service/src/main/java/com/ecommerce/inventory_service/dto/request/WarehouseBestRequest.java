package com.ecommerce.inventory_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseBestRequest {
    String customerLatitude;  // Tọa độ khách hàng
    String customerLongitude; // Tọa độ khách hàng
    Set<ProductCheckout> productCheckouts;
}