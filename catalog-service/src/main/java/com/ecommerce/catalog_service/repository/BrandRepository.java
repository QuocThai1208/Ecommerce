package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, String> {
}