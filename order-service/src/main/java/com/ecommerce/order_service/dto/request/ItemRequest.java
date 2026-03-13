package com.ecommerce.order_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    String productVariantId;
    String productNameSnapshot;
    BigDecimal unitPriceSnapshot;
    String mediaUrl;
    long quantity;
}