package com.ecommerce.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShipmentCreationEvent {
    String orderId;
    String carrierId;
    String shopPickupPointId;
    String userAddressId;
    BigDecimal codAmount;
    List<GhnItem> items;
}