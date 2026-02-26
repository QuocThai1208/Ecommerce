package com.ecommerce.inventory_service.dto.request;

import com.ecommerce.inventory_service.entity.Warehouse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoriesRequest {
    String productVariantId;
    long quantityAvailable;
    long quantityReserved;
    Warehouse warehouse;
}