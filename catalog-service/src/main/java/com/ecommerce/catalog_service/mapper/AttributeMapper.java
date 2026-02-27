package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.AttributeRequest;
import com.ecommerce.catalog_service.dto.request.AttributeUpdateRequest;
import com.ecommerce.catalog_service.dto.request.BrandRequest;
import com.ecommerce.catalog_service.dto.request.BrandUpdateRequest;
import com.ecommerce.catalog_service.dto.response.AttributeResponse;
import com.ecommerce.catalog_service.dto.response.BrandResponse;
import com.ecommerce.catalog_service.entity.Attribute;
import com.ecommerce.catalog_service.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
    Attribute toAttribute(AttributeRequest request);
    @Mapping(target = "brandName", source = "brand.name")
    AttributeResponse toAttributeResponse(Attribute attribute);
    void updateAttribute(@MappingTarget Attribute attribute, AttributeUpdateRequest request);
    List<AttributeResponse> toAttributeResponseList(List<Attribute> attributeList);
}