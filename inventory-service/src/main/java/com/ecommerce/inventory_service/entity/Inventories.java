package com.ecommerce.inventory_service.entity;

import com.ecommerce.inventory_service.util.InventoryId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Inventories {
    @Id
    @InventoryId
    String id;
    String productVariantId;

    @Builder.Default
    long quantityAvailable = 0;
    @Builder.Default
    long quantityReserved = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseId")
    Warehouse warehouse;
    Instant created_at;
    Instant update_at;
}