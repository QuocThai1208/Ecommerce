package com.ecommerce.shipment_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculatorFeeItemRequest {
    String shopPickupPointId;
    Integer weight; // gram
    Integer length;
    Integer width;
    Integer height;
    Integer insuranceValue;
}