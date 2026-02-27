package com.ecommerce.payment_service.repository;

import com.ecommerce.payment_service.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, String> {
}