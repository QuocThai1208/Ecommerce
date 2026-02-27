package com.ecommerce.shipment_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarrierResponse {
    String id;  // GHN
    String name; // Tên đơn vị vận chuyển
    Boolean isActive; // trạng thái hoạt động
    Instant createdAt;
    Instant updateAt;
}