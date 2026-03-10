package com.ecommerce.order_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculatorFeeRequest {
    String carrierId;
    String shopPickupPointId;
    String userAddressId;
    Integer weight; // gram
    Integer length;
    Integer width;
    Integer height;
    Integer insuranceValue;
}