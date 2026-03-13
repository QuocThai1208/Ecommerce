package com.ecommerce.order_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculatorFeeItemResponse {
    String shopPickupPointId;
    Integer fee;
}