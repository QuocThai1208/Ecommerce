package com.ecommerce.shipment_service.dto.response;

import com.ecommerce.shipment_service.enums.ShipmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShipmentResponse {
    String id;
    String orderId;
    String trackingNumber;
    String carrierName;
    String shopAddress;
    String userAddress;
    ShipmentStatus status;
    Instant estimatedDelivery;
    BigDecimal codAmount;
    BigDecimal shippingFee;
    Instant createdAt;
    Instant updateAt;
}