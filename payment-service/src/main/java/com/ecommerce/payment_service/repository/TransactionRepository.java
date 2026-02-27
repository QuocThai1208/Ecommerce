package com.ecommerce.payment_service.repository;

import com.ecommerce.payment_service.entity.Transaction;
import com.ecommerce.payment_service.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByIdAndStatus(String transactionId, TransactionStatus status);
}