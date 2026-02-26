package com.ecommerce.inventory_service.entity;

import com.ecommerce.inventory_service.util.WarehouseId;
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
        name = "warehouse",
        indexes = {
                @Index(name = "idx_brand_id", columnList = "brandId")
        }
)
public class Warehouse {
    @Id
    @WarehouseId
    String id;
    String name;
    String brandId;
    String wardCode;
    String addressDetail;
    Double latitude; // vĩ độ
    Double longitude; // kinh độ
    String contactName; // Tên người đại diện của kho
    String contactPhone; // Sdt của người đại diện
    Instant created_at;
    Instant update_at;
}