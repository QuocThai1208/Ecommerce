package com.ecommerce.shipment_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopPickupPointRequest {
    String warehouseId; // id của kho hàng từ inventory
    String brandId; // id của brand từ catalog
    String contactName; // Tên người đại diện của kho
    String contactPhone; // Sdt của người đại diện
    String wareCode;
    String addressDetail; // địa chỉ chi tiết
}