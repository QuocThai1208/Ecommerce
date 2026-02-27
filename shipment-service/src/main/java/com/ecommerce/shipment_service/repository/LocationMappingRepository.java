package com.ecommerce.shipment_service.repository;

import com.ecommerce.shipment_service.entity.CarrierLocationMapping;
import com.ecommerce.shipment_service.entity.Carriers;
import com.ecommerce.shipment_service.entity.MasterLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationMappingRepository extends JpaRepository<CarrierLocationMapping, String> {
    Optional<CarrierLocationMapping> findByMasterLocation(MasterLocation masterLocation);
    Optional<CarrierLocationMapping> findByMasterLocationAndCarriers(MasterLocation masterLocation, Carriers carriers);
}