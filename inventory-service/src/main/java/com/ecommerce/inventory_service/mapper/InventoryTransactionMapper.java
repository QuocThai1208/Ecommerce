package com.ecommerce.inventory_service.mapper;

import com.ecommerce.inventory_service.dto.request.InventoryTransactionRequest;
import com.ecommerce.inventory_service.dto.response.InventoryTransactionResponse;
import com.ecommerce.inventory_service.entity.InventoryTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface InventoryTransactionMapper {
    @Mapping(target = "inventory", ignore = true)
    InventoryTransaction toInventoryTransaction(InventoryTransactionRequest request);

    InventoryTransactionResponse toInventoryTransactionResponse(InventoryTransaction inventoryTransaction);

    List<InventoryTransactionResponse> toInventoryTransactionResponseList(List<InventoryTransaction> inventoryTransactionList);
}