package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.Product;
import feign.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByBrandId(String brandId);
    Optional<Product> findBySlugAndBrandId(String slug, String brandId);

    @EntityGraph(attributePaths = {"categories"})
    Optional<Product> findBySlug(String productSlug);
}