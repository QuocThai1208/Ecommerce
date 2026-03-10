package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    List<ProductVariant> findAllByProductSlug(String productSlug);
}