package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByBrandId(String brandId);
    Optional<Product> findBySlugAndBrandId(String slug, String brandId);
}