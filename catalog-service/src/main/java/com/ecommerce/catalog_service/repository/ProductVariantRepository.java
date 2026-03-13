package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.ProductVariant;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    List<ProductVariant> findAllByProductSlug(String productSlug);

    @Query("SELECT DISTINCT v FROM ProductVariant v " +
            "LEFT JOIN FETCH v.productMedia " +
            "LEFT JOIN FETCH v.attributeValues av " +
            "LEFT JOIN FETCH av.attribute a " +
            "WHERE v.product.slug = :productSlug")
    List<ProductVariant> findAllByProductSlugWithDetails(@Param("productSlug") String productSlug);

    @Query("SELECT v FROM ProductVariant v " +
            "JOIN FETCH v.product p " +
            "JOIN FETCH p.brand b " +
            "LEFT JOIN FETCH v.productMedia " +
            "WHERE v.sku IN :variantIds")
    List<ProductVariant> findAllBySkuInWithDetails(@Param("variantIds") Set<String> variantIds);
}