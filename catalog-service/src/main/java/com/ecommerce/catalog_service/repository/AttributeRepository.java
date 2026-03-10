package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.Attribute;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, String> {
    @EntityGraph(attributePaths = {"values"})
    List<Attribute> findAllByBrandId(String brandId);
}