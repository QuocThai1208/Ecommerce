package com.ecommerce.inventory_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseRequest {
    String name;
    String brandId;
    String brandName;
    String wardCode;
    String districtCode;
    String provinceCode;
    String addressDetail;
    Double latitude; // vĩ độ
    Double longitude; // kinh độ
    String contactName; // Tên người đại diện của kho
    String contactPhone; // Sdt của người đại diện
}