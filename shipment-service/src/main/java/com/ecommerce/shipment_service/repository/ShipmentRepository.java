package com.ecommerce.shipment_service.repository;

import com.ecommerce.shipment_service.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}