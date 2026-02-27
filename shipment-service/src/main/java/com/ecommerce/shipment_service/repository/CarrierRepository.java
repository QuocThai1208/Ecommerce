package com.ecommerce.shipment_service.repository;

import com.ecommerce.shipment_service.entity.Carriers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrierRepository extends JpaRepository<Carriers, String> {
}