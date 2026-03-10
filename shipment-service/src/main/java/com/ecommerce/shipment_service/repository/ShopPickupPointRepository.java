package com.ecommerce.shipment_service.repository;

import com.ecommerce.shipment_service.entity.ShopPickupPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopPickupPointRepository extends JpaRepository<ShopPickupPoint, String> {

}