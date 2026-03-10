package com.ecommerce.inventory_service.mapper;

import com.ecommerce.inventory_service.dto.request.InventoriesRequest;
import com.ecommerce.inventory_service.dto.response.InventoriesResponse;
import com.ecommerce.inventory_service.entity.Inventories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoriesMapper {
    Inventories inventoriesEventToInventories(InventoriesRequest request);

    @Mapping(target = "warehouseId", source = "warehouse.id")
    InventoriesResponse toInventoriesResponse(Inventories inventories);
    List<InventoriesResponse> toInventoriesResponseList(List<Inventories> inventories);
}