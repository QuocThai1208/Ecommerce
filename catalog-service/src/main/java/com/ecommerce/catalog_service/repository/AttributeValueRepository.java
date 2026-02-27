
package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, String> {
}