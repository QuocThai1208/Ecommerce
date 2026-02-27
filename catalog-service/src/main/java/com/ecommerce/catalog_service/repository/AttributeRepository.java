package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, String> {
}