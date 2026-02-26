package com.ecommerce.inventory_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckRequest {
    String productVariantId;
    long quantity;
    String warehouseId;
}