package com.ecommerce.catalog_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TotalAvailableResponse {
    String variantId;
    long totalAvailable;  // Lượng hàng hiện có trong kho
}