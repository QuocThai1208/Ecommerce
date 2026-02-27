package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}