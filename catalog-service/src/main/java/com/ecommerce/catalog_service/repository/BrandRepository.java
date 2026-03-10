package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, String> {
    Optional<Brand> findByUserId(String userId);
}