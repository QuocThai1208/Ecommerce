package com.ecommerce.inventory_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseResponse {
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