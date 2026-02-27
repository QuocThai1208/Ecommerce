package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.request.WarehouseRequest;
import com.ecommerce.inventory_service.dto.request.WarehouseUpdateRequest;
import com.ecommerce.inventory_service.dto.response.WarehouseResponse;
import com.ecommerce.inventory_service.entity.Warehouse;
import com.ecommerce.inventory_service.exception.AppException;
import com.ecommerce.inventory_service.exception.ErrorCode;
import com.ecommerce.inventory_service.mapper.WarehouseMapper;
import com.ecommerce.inventory_service.repository.WarehouseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseService {
    WarehouseRepository warehouseRepository;
    WarehouseMapper warehouseMapper;

    public WarehouseResponse create(WarehouseRequest request){
        Warehouse warehouse = warehouseMapper.toWarehouse(request);
        warehouse.setCreated_at(Instant.now());
        warehouse.setUpdate_at(Instant.now());
        return warehouseMapper.toWarehouseResponse(warehouseRepository.save(warehouse));
    }

    public WarehouseResponse getWarehouse(String warehouseId){
        var warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));
        return warehouseMapper.toWarehouseResponse(warehouse);
    }

    public List<WarehouseResponse> getAllWarehouse(){
        var warehouses = warehouseRepository.findAll();
        return warehouseMapper.toWarehouseResponseList(warehouses);
    }

    public WarehouseResponse update(String warehouseId, WarehouseUpdateRequest request){
        var warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED));
        warehouseMapper.updateWarehouse(warehouse, request);
        warehouse.setUpdate_at(Instant.now());
        return warehouseMapper.toWarehouseResponse(warehouseRepository.save(warehouse));
    }

    public String delete(String warehouseId){
        if(!warehouseRepository.existsById(warehouseId)){
            throw new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED);
        }
        warehouseRepository.deleteById(warehouseId);
        return "Delete warehouse success.";
    }
}