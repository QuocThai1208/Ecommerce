package com.ecommerce.order_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemBatchDetailResponse {
    String productVariantId;
    String productNameSnapshot;
    BigDecimal unitPriceSnapshot;
}