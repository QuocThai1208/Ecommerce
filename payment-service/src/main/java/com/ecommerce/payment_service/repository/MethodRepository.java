package com.ecommerce.payment_service.repository;

import com.ecommerce.payment_service.entity.Method;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MethodRepository extends JpaRepository<Method, String> {
    Optional<Method> findByUserIdAndIsDefaultTrue(String userId);
    List<Method> findAllByUserId(String userId);
    Optional<Method> findByIdAndIsActiveTrue(String methodId);
}