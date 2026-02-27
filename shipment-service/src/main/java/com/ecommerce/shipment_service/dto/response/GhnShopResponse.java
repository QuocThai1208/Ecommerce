package com.ecommerce.shipment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnShopResponse {
    int code;
    String message;
    ghnData data;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ghnData{
        @JsonProperty("shop_id")
        int shopId;
    }
}