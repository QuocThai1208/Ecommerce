package com.ecommerce.shipment_service.repository;

import com.ecommerce.shipment_service.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, String> {
    Optional<UserAddress> findByUserIdAndIsDefaultTrue(String userId);

    List<UserAddress> findAllByUserId(String userId);
    Optional<UserAddress> findByIdAndUserId(String id, String userId);
}