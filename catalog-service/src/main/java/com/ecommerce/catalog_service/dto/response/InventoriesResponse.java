package com.ecommerce.catalog_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String warehouseId;
}