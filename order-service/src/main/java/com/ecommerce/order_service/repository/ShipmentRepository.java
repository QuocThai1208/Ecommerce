package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.entity.OrderShipments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<OrderShipments, String> {
    Optional<OrderShipments> findByIdAndOrderId(String shipmentId, String orderId);
    List<OrderShipments> findAllByOrderId(String orderId);
    boolean existsByOrderId(String orderId);

}