package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.AttributeValueRequest;
import com.ecommerce.catalog_service.dto.request.AttributeValueUpdateRequest;
import com.ecommerce.catalog_service.dto.response.AttributeValueResponse;
import com.ecommerce.catalog_service.entity.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {
    @Mapping(target = "attribute", ignore = true)
    AttributeValue toAttributeValue(AttributeValueRequest request);

    @Mapping(target = "brandName", source = "brand.name")
    AttributeValueResponse toAttributeValueResponse(AttributeValue attributeValue);

    @Mapping(target = "attribute", ignore = true)
    void updateAttributeValue(@MappingTarget AttributeValue attributeValue, AttributeValueUpdateRequest request);

    List<AttributeValueResponse> toAttributeResponseList(List<AttributeValue> attributeList);
}