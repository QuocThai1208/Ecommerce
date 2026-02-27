package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.ProductVariantRequest;
import com.ecommerce.catalog_service.dto.request.ProductVariantUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ProductVariantResponse;
import com.ecommerce.catalog_service.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    @Mapping(target = "attributeValues", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "productMedia", ignore = true)
    ProductVariant toProductVariant(ProductVariantRequest request);

    @Mapping(target = "media", source = "productMedia.mediaUrl")
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    List<ProductVariantResponse> toProductVariantResponseList(List<ProductVariant> productVariantList);

    void updateProductVariant(@MappingTarget ProductVariant productVariant, ProductVariantUpdateRequest request);


}