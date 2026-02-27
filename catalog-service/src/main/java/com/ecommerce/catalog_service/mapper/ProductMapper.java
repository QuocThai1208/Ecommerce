package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "active", source = "isActive")
    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> productList);

    @Mapping(target = "categories", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
}