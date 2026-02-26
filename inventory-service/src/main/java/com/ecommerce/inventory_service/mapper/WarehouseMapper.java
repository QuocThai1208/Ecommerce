package com.ecommerce.inventory_service.mapper;

import com.ecommerce.inventory_service.dto.request.WarehouseRequest;
import com.ecommerce.inventory_service.dto.request.WarehouseUpdateRequest;
import com.ecommerce.inventory_service.dto.response.WarehouseResponse;
import com.ecommerce.inventory_service.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    Warehouse toWarehouse(WarehouseRequest request);
    WarehouseResponse toWarehouseResponse(Warehouse warehouse);
    List<WarehouseResponse> toWarehouseResponseList(List<Warehouse> warehouseList);

    void updateWarehouse(@MappingTarget Warehouse warehouse, WarehouseUpdateRequest request);
}