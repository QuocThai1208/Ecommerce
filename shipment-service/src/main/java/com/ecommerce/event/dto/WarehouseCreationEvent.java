package com.ecommerce.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseCreationEvent {
    String warehouseId;
    String brandId; // id của brand từ catalog
    String contactName; // Tên người đại diện của kho
    String contactPhone; // Sdt của người đại diện
    String wardCode; // địa chỉ
    String addressDetail; // địa chỉ chi tiết
}