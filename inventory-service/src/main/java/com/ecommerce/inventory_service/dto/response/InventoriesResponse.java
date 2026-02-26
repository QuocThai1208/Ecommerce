package com.ecommerce.inventory_service.dto.response;

import com.ecommerce.inventory_service.entity.Warehouse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoriesResponse {
    String id;
    String productVariantId;
    long quantityAvailable;  // Lượng hàng hiện có trong kho
    long quantityReserved; // Lượng hàng bị giữ lại
    Warehouse warehouse;
    Instant created_at;
    Instant update_at;
}