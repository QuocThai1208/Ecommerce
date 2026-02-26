package com.ecommerce.inventory_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseUpdateRequest {
    String name;
    String location;
    double latitude; // vĩ độ
    double longitude; // kinh độ
}