package com.ecommerce.shipment_service.dto.request;

import com.ecommerce.event.dto.GhnItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnCreateOrderRequest {
    Integer payment_type_id; // 1: Shop trả, 2: Khách trả
    String note;
    String required_note; // CHOPHEPXEMHANG, CHOXEMHANGKHONGTHU, KHONGCHOXEMHANG
    String client_order_code; // orderId của bạn
    String to_name;
    String to_phone;
    String to_address;
    String to_ward_code;
    Integer to_district_id;
    Integer cod_amount;
    String content;
    Integer weight; // gram
    Integer length;
    Integer width;
    Integer height;
    Integer service_type_id; // 2: E-commerce (Chuyển phát thương mại điện tử)
    List<GhnItem> items;
}