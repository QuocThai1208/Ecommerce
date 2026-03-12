package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ProductDetailResponse;
import com.ecommerce.catalog_service.dto.response.ProductDisplayDetailResponse;
import com.ecommerce.catalog_service.dto.response.ProductDisplayResponse;
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
    @Mapping(target = "categories", ignore = true)
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "categories", ignore = true)
    ProductDetailResponse toProductDetailResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> productList);

    @Mapping(target = "categories", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);

    ProductDisplayResponse toProductDisplayResponse(Product product);
    List<ProductDisplayResponse> toProductDisplayResponseList(List<Product> products);

    ProductDisplayDetailResponse toProductDisplayDetailResponse(Product product);

}