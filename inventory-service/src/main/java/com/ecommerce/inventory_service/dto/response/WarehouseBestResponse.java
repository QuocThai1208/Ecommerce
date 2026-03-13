package com.ecommerce.inventory_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseBestResponse {
    String warehouseId;
    String brandId;
    Set<ProductAssignment> productAssignments;
}