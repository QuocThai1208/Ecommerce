package com.ecommerce.shipment_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarrierCreationRequest {
    String id;  // GHN
    String name; // Tên đơn vị vận chuyển
}