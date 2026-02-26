package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.entity.Inventories;
import com.ecommerce.inventory_service.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, String> {
    List<InventoryTransaction> findByInventory(Inventories inventory);
    List<InventoryTransaction> findAllByOrderId(String orderId);
}