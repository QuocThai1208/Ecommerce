package com.ecommerce.shipment_service.dto.response;

import com.ecommerce.shipment_service.enums.LocationType;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MasterLocationResponse {
    String codename;
    String name; // tên thành phố/quận
    LocationType type; // cấp của địa chỉ
}