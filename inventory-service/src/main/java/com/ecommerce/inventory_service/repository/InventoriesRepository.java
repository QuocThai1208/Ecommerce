package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.entity.Inventories;
import com.ecommerce.inventory_service.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InventoriesRepository extends JpaRepository<Inventories, String> {
    Optional<Inventories> findByProductVariantIdAndWarehouse(String productVariantId, Warehouse warehouse);
    Optional<Inventories> findByProductVariantIdAndWarehouseId(String productVariantId, String warehouseId);

    @Query("""
    """)
    List<Inventories> findAllByProductVariantIdIn(Set<String> productVariantId);

}