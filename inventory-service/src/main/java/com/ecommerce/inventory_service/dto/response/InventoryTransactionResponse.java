package com.ecommerce.inventory_service.dto.response;

import com.ecommerce.inventory_service.entity.Inventories;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryTransactionResponse {
    String id;
    Inventories inventory;
    long qualityChange;
    String referentId;
    Instant created_at;
    Instant update_at;
}