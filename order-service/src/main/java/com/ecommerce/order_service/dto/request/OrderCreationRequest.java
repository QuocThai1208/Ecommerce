package com.ecommerce.order_service.dto.request;

import com.ecommerce.order_service.enums.MethodType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    String userAddressId;
    String customerLatitude;
    String customerLongitude;
    MethodType method;
    List<BrandOrderRequest> brandOrderRequest;
}