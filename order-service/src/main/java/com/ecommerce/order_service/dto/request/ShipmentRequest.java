package com.ecommerce.order_service.dto.request;

import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.enums.OrderStatus;
import com.ecommerce.order_service.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShipmentRequest {
    String trackingNumber;
    String carrierName;
    ShipmentStatus shipmentStatus;
    Instant shipmentDate;
    Date estimatedDeliveryDate;
}