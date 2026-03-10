package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, String> {
}