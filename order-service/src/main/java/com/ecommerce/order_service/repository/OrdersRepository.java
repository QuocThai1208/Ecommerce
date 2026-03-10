package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.entity.Orders;
import com.ecommerce.order_service.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;


public interface OrdersRepository extends JpaRepository<Orders, String> {
    Page<Orders> findAllByUserId(String userId, Pageable pageable);

    Page<Orders> findAllByUserIdAndStatus(String userId, OrderStatus status, Pageable pageable);

    @Query("""
        SELECT o FROM Orders o
        WHERE o.status = COALESCE(:status, o.status)
        AND o.userId = COALESCE(:userId, o.userId)
        AND o.createdAt >= COALESCE(:fromDate, o.createdAt)
        AND o.createdAt <= COALESCE(:toDate, o.createdAt)
        ORDER BY o.createdAt DESC
    """)
    Page<Orders> findByFilter(OrderStatus status, String userId, Instant fromDate, Instant toDate, Pageable pageable);
}