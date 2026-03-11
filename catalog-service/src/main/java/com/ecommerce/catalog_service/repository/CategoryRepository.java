package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Set<Category> findAllByIdIn(Set<String> ids);
}