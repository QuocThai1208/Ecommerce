package com.ecommerce.inventory_service.entity;

import com.ecommerce.inventory_service.type.TransactionType;
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
@Table(
        name = "inventory_transaction",
        indexes = {
                @Index(name = "idx_transaction_order_id", columnList = "orderId")
        }
)
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    Inventories inventory;
    long qualityChange;
    String referentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionType transactionType;

    String orderId; // id của bảng order trong order service
    Instant created_at;
    Instant update_at;
}