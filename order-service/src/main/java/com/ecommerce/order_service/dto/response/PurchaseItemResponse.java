package com.ecommerce.order_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseItemResponse {
    String id;
    String productVariantId;
    String productNameSnapshot;
    String variantNameSnapshot;
    String mediaUrl;
    String unitPriceSnapshot;
    long quantity;
    BigDecimal totalItemAmount;
    Instant createdAt;
}