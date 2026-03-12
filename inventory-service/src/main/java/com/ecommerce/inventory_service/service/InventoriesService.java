package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.request.CheckRequest;
import com.ecommerce.inventory_service.dto.request.InventoriesRequest;
import com.ecommerce.inventory_service.dto.response.InventoriesResponse;
import com.ecommerce.inventory_service.dto.response.TotalAvailableResponse;
import com.ecommerce.inventory_service.entity.Inventories;
import com.ecommerce.inventory_service.exception.AppException;
import com.ecommerce.inventory_service.exception.ErrorCode;
import com.ecommerce.inventory_service.mapper.InventoriesMapper;
import com.ecommerce.inventory_service.repository.InventoriesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoriesService {
    InventoriesRepository inventoriesRepository;
    InventoriesMapper inventoriesMapper;

    // Tạo mới inventory
    public Inventories create(InventoriesRequest request) {
        var now = Instant.now();
        Inventories inventories = inventoriesMapper.inventoriesEventToInventories(request);
        inventories.setCreated_at(now);
        inventories.setUpdate_at(now);
        return inventoriesRepository.save(inventories);
    }

    // Nhập kho
    public void inflow(Inventories inventory, long qualityChange) {
        inventory.setQuantityAvailable(inventory.getQuantityAvailable() + qualityChange);
        inventoriesRepository.save(inventory);
    }

    // Đặt trước
    public void reserve(Inventories inventory, long qualityChange) {
        if (inventory.getQuantityAvailable() < qualityChange)
            throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);

        inventory.setQuantityAvailable(inventory.getQuantityAvailable() - qualityChange);
        inventory.setQuantityReserved(inventory.getQuantityReserved() + qualityChange);
        inventory.setUpdate_at(Instant.now());
        inventoriesRepository.save(inventory);
    }

    // Hủy đặt trước
    public void release(Inventories inventory, long qualityChange) {
        if (inventory.getQuantityReserved() < qualityChange)
            throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);

        inventory.setQuantityAvailable(inventory.getQuantityAvailable() + qualityChange);
        inventory.setQuantityReserved(inventory.getQuantityReserved() - qualityChange);
        inventory.setUpdate_at(Instant.now());
        inventoriesRepository.save(inventory);
    }

    // Xuất kho trực tiếp
    public void directOutflow(Inventories inventory, long qualityChange) {
        if (inventory.getQuantityAvailable() < qualityChange)
            throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);
        inventory.setQuantityAvailable(inventory.getQuantityAvailable() - qualityChange);
        inventory.setUpdate_at(Instant.now());
        inventoriesRepository.save(inventory);
    }

    // Xuất kho từ số lượng đã đặt
    public void outflow(Inventories inventory, long qualityChange) {
        if (inventory.getQuantityReserved() < qualityChange)
            throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);
        inventory.setQuantityReserved(inventory.getQuantityReserved() - qualityChange);
        inventory.setUpdate_at(Instant.now());
        inventoriesRepository.save(inventory);
    }

    // Lấy danh sách inventories
    public List<InventoriesResponse> getInventories() {
        var inventories = inventoriesRepository.findAll();
        return inventoriesMapper.toInventoriesResponseList(inventories);
    }

    // Lấy inventory
    public InventoriesResponse getInventory(String inventoryId) {
        var inventory = inventoriesRepository.findById(inventoryId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_EXISTED));

        return inventoriesMapper.toInventoriesResponse(inventory);
    }

    // Kiểm tra số lượng còn đủ không (được gọi từ order)
    public Boolean check(CheckRequest request) {
        if (request.getQuantity() <= 0) throw new AppException(ErrorCode.QUANTITY_CHECK_INVALID);
        var inventory = inventoriesRepository.findByProductVariantIdAndWarehouseId(request.getProductVariantId(), request.getWarehouseId())
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_EXISTED));
        return inventory.getQuantityAvailable() >= request.getQuantity();
    }

    public List<InventoriesResponse> getByVariantId(Set<String> variantIds){
        var inventories = inventoriesRepository.findAllByProductVariantIdIn(variantIds);
        return inventoriesMapper.toInventoriesResponseList(inventories);
    }

    public List<TotalAvailableResponse> getTotalAvailable(Set<String> variantIds){
        var inventories = inventoriesRepository.findAllByProductVariantIdIn(variantIds);

        Map<String, Long> stockMap = inventories.stream()
                .collect(Collectors.groupingBy(
                        Inventories::getProductVariantId,
                        Collectors.summingLong(Inventories::getQuantityAvailable)
                ));

        return stockMap.entrySet().stream()
                .map(entry -> {
                    return TotalAvailableResponse.builder()
                            .variantId(entry.getKey())
                            .totalAvailable(entry.getValue())
                            .build();
                }).toList();
    }
}