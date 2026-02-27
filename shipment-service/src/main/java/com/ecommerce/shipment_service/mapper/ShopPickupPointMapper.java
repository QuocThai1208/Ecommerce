package com.ecommerce.shipment_service.mapper;

import com.ecommerce.event.dto.WarehouseCreationEvent;
import com.ecommerce.shipment_service.dto.response.ShopPickupPointResponse;
import com.ecommerce.shipment_service.entity.ShopPickupPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShopPickupPointMapper {
    @Mapping(target = "id", source = "warehouseId")
    @Mapping(target = "wardCode", ignore = true)
    ShopPickupPoint toShopPickupPoint(WarehouseCreationEvent event);

    ShopPickupPointResponse toShopPickupPointResponse(ShopPickupPoint shopPickupPoint);
}