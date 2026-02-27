package com.ecommerce.shipment_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnFeeRequest {
    String to_ward_code;
    Integer to_district_id;
    Integer weight; // gram
    Integer length;
    Integer width;
    Integer height;
    Integer insuranceValue;
    Integer service_type_id; // 2: E-commerce (Chuyển phát thương mại điện tử)
}