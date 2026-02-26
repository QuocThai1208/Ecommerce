package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.ApiResponse;
import com.ecommerce.inventory_service.dto.request.WarehouseRequest;
import com.ecommerce.inventory_service.dto.request.WarehouseUpdateRequest;
import com.ecommerce.inventory_service.dto.response.WarehouseResponse;
import com.ecommerce.inventory_service.service.WarehouseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/warehouses")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseController {
    WarehouseService warehouseService;

    @PostMapping
    ApiResponse<WarehouseResponse> create(@RequestBody WarehouseRequest request){
        return ApiResponse.<WarehouseResponse>builder()
                .message("Create warehouse success.")
                .result(warehouseService.create(request))
                .build();
    }

    @GetMapping("/{warehouseId}")
    ApiResponse<WarehouseResponse> getWarehouse(@PathVariable String warehouseId){
        return ApiResponse.<WarehouseResponse>builder()
                .message("Get warehouse success.")
                .result(warehouseService.getWarehouse(warehouseId))
                .build();
    }

    @GetMapping
    ApiResponse<List<WarehouseResponse>> getAllWarehouse(){
        return ApiResponse.<List<WarehouseResponse>>builder()
                .message("Get warehouse success.")
                .result(warehouseService.getAllWarehouse())
                .build();
    }

    @PutMapping("/{warehouseId}")
    ApiResponse<WarehouseResponse> update(@PathVariable String warehouseId, @RequestBody WarehouseUpdateRequest request){
        return ApiResponse.<WarehouseResponse>builder()
                .message("Update warehouse success.")
                .result(warehouseService.update(warehouseId, request))
                .build();
    }

    @DeleteMapping("/{warehouseId}")
    ApiResponse<String> delete(@PathVariable String warehouseId){
        return ApiResponse.<String>builder()
                .message("Update warehouse success.")
                .result(warehouseService.delete(warehouseId))
                .build();
    }
}