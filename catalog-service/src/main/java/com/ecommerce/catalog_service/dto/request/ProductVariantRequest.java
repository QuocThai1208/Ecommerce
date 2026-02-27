package com.ecommerce.catalog_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantRequest {
    Set<String> attributeValueIds;
    String productId;
    String fileName;
    long priceAdjustment;
    String productVariantId;
    Set<String> warehouseIds;
}