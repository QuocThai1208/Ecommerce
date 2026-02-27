package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.request.AttributeRequest;
import com.ecommerce.catalog_service.dto.request.AttributeUpdateRequest;
import com.ecommerce.catalog_service.dto.response.AttributeResponse;
import com.ecommerce.catalog_service.entity.Attribute;
import com.ecommerce.catalog_service.entity.Brand;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.AttributeMapper;
import com.ecommerce.catalog_service.repository.AttributeRepository;
import com.ecommerce.catalog_service.repository.BrandRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeService {
    AttributeRepository attributeRepository;
    AttributeMapper attributeMapper;

    BrandRepository brandRepository;
    public AttributeResponse createAttribute(AttributeRequest request){
        Attribute attribute = attributeMapper.toAttribute(request);
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        attribute.setBrand(brand);
        return attributeMapper.toAttributeResponse(attributeRepository.save(attribute));
    }

    public AttributeResponse getAttribute(String attributeId){
        var attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_EXISTED));
        return attributeMapper.toAttributeResponse(attribute);
    }

    public List<AttributeResponse> getAllAttribute(){
        var attributes = attributeRepository.findAll();
        return attributeMapper.toAttributeResponseList(attributes);
    }

    public AttributeResponse updateAttribute(String attributeId, AttributeUpdateRequest request){
        var attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_EXISTED));

        attributeMapper.updateAttribute(attribute, request);
        return attributeMapper.toAttributeResponse(attributeRepository.save(attribute));
    }
}