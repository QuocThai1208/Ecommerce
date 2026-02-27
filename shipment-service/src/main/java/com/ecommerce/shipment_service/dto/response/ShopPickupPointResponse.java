package com.ecommerce.shipment_service.dto.response;

import com.ecommerce.shipment_service.entity.Carriers;
import com.ecommerce.shipment_service.entity.MasterLocation;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopPickupPointResponse {
    String id;
    String warehouseId; // id của kho hàng từ inventory
    String brandId; // id của brand từ catalog
    String carrierName;
    String carrierShopId; // id do đơn vị vận chuyển cung cấp
    String contactName; // Tên người đại diện của kho
    String contactPhone; // Sdt của người đại diện
    String address; // địa chỉ chi tiết
    Instant createdAt;
    Instant updateAt;
}