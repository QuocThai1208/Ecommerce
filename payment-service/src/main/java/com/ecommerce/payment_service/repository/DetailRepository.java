package com.ecommerce.payment_service.repository;

import com.ecommerce.payment_service.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface DetailRepository extends JpaRepository<TransactionDetail, String> {
    Optional<TransactionDetail> findByOrderId(String orderId);
}