package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.entity.OrderPayment;
import com.ecommerce.order_service.enums.PaymentStatus;
import com.ecommerce.order_service.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<OrderPayment, String> {
    Optional<OrderPayment> findByOrderIdAndStatusAndTransactionType(String orderId, PaymentStatus status, TransactionType type);
    List<OrderPayment> findAllByOrderId(String orderId);
    Optional<OrderPayment> findByOrderIdAndPaymentTransactionIdAndStatus(String orderId, String paymentTransactionId, PaymentStatus status);
    Optional<OrderPayment> findByOrderIdAndTransactionTypeAndStatus(String orderId, TransactionType type, PaymentStatus status);
}