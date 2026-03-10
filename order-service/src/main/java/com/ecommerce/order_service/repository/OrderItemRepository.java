package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    Page<OrderItem> findAllByOrderId(String orderId, Pageable pageable);
}