package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.ProductMedia;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductMediaRepository extends JpaRepository<ProductMedia, String> {
    boolean existsByProductSlugAndIsMainTrue(String productId);

    @Query("SELECT COALESCE(MAX(p.sortOrder), -1) FROM ProductMedia p WHERE p.product.slug = :productId")
    Integer findMaxSortOrderByProductId(@Param("productId") String productId);
}