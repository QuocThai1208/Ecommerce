package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.ProductVariantRequest;
import com.ecommerce.catalog_service.dto.request.ProductVariantUpdateRequest;
import com.ecommerce.catalog_service.dto.response.AttributeValueResponse;
import com.ecommerce.catalog_service.dto.response.ProductVariantResponse;
import com.ecommerce.catalog_service.dto.response.VariantDisplayResponse;
import com.ecommerce.catalog_service.dto.response.VariantInflowResponse;
import com.ecommerce.catalog_service.entity.AttributeValue;
import com.ecommerce.catalog_service.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    @Mapping(target = "attributeValues", ignore = true)
    ProductVariant toProductVariant(ProductVariantRequest request);

    AttributeValueResponse toAttributeValueResponse(AttributeValue attributeValue);

    Set<AttributeValueResponse> toAttributeValueResponseSet(Set<AttributeValue> attributeValue);

    @Mapping(target = "media", source = "productMedia.mediaUrl")
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    List<ProductVariantResponse> toProductVariantResponseList(List<ProductVariant> productVariantList);

    void updateProductVariant(@MappingTarget ProductVariant productVariant, ProductVariantUpdateRequest request);

    @Mapping(target = "media", source = "productMedia.mediaUrl")
    VariantInflowResponse toVariantInflowResponse(ProductVariant variant);

    List<VariantInflowResponse> toVariantInflowResponseList(List<ProductVariant> variants);

    @Mapping(target = "urlMedia", source = "productMedia.mediaUrl")
    @Mapping(target = "quantityAvailable", ignore = true)
    @Mapping(target = "values", source = "attributeValues")
    VariantDisplayResponse toVariantDisplayResponse(ProductVariant variant);

    List<VariantDisplayResponse> toVariantDisplayResponseList(List<ProductVariant> variants);

    default List<String> mapAttributeValues(Set<AttributeValue> values){
        if (values==null) return null;
        return values.stream().map(AttributeValue::getValueCode).toList();
    }
}