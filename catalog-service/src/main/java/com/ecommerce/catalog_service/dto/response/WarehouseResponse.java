package com.ecommerce.catalog_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseResponse {
    String id;
    String name;
    String wardCode;
    String districtCode;
    String provinceCode;
    String addressDetail;
    Double latitude; // vĩ độ
    Double longitude; // kinh độ
    String contactName; // Tên người đại diện của kho
    String contactPhone; // Sdt của người đại diện
}