package com.ecommerce.shipment_service.repository;

import com.ecommerce.shipment_service.entity.MasterLocation;
import com.ecommerce.shipment_service.enums.LocationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MasterLocationRepository extends JpaRepository<MasterLocation, String> {
    List<MasterLocation> findAllByType(LocationType type);
    Page<MasterLocation> findAllByType(LocationType type, Pageable pageable);
    Page<MasterLocation> findAllByParentCode(MasterLocation parentCode, Pageable pageable);
    List<MasterLocation> findAllByParentCode(MasterLocation parentCode);
}