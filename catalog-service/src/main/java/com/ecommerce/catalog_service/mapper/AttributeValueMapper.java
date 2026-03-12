package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.AttributeValueRequest;
import com.ecommerce.catalog_service.dto.request.AttributeValueUpdateRequest;
import com.ecommerce.catalog_service.dto.response.AttributeValueResponse;
import com.ecommerce.catalog_service.dto.response.ProductOptionResponse;
import com.ecommerce.catalog_service.entity.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {
    @Mapping(target = "attribute", ignore = true)
    AttributeValue toAttributeValue(AttributeValueRequest request);

    AttributeValueResponse toAttributeValueResponse(AttributeValue attributeValue);

    List<AttributeValueResponse> toAttributeResponseList(List<AttributeValue> attributeList);

    @Mapping(target = "attribute", ignore = true)
    void updateAttributeValue(@MappingTarget AttributeValue attributeValue, AttributeValueUpdateRequest request);


    default List<ProductOptionResponse> toProductOptionResponseList(Set<AttributeValue> values){
        return values.stream()
                .collect(Collectors.groupingBy(v -> v.getAttribute().getName()))
                .entrySet().stream()
                .map(entry -> ProductOptionResponse.builder()
                        .name(entry.getKey())
                        .values(toAttributeResponseList(entry.getValue()))
                        .build()).toList();
    }
}