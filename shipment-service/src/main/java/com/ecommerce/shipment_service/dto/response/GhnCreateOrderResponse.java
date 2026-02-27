package com.ecommerce.shipment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnCreateOrderResponse {
    int code;
    String message;
    private OrderData data;

    @Data
    public static class OrderData {
        @JsonProperty("order_code")
        private String orderCode;

        @JsonProperty("total_fee")
        private BigDecimal totalFee;

        @JsonProperty("expected_delivery_time")
        private String expectedDeliveryTime;
    }
}