package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.request.AttributeValueRequest;
import com.ecommerce.catalog_service.dto.request.AttributeValueUpdateRequest;
import com.ecommerce.catalog_service.dto.response.AttributeValueResponse;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.AttributeValueMapper;
import com.ecommerce.catalog_service.repository.AttributeRepository;
import com.ecommerce.catalog_service.repository.AttributeValueRepository;
import com.ecommerce.catalog_service.repository.BrandRepository;
import com.ecommerce.catalog_service.util.SlugUtils;
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
public class AttributeValueService {
    AttributeValueRepository attributeValueRepository;
    AttributeValueMapper attributeValueMapper;
    AttributeRepository attributeRepository;

    BrandRepository brandRepository;

    public AttributeValueResponse createAttributeValue(AttributeValueRequest request){
        var attributeValue = attributeValueMapper.toAttributeValue(request);
        var attribute = attributeRepository.findById(request.getAttributeId())
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_EXISTED));
        attributeValue.setAttribute(attribute);
        attributeValue.setBrand(attribute.getBrand());
        attributeValue.setValueCode(SlugUtils.toUnaccentUpperCase(request.getValue()));
        return attributeValueMapper.toAttributeValueResponse(attributeValueRepository.save(attributeValue));
    }

    public AttributeValueResponse getAttributeValue(String attributeValueId){
        var attributeValue = attributeValueRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_EXISTED));
        return attributeValueMapper.toAttributeValueResponse(attributeValue);
    }

    public List<AttributeValueResponse> getAllAttributeValue(){
        return attributeValueMapper.toAttributeResponseList(
                attributeValueRepository.findAll()
        );
    }

    public AttributeValueResponse updateAttributeValue(String attributeValueId, AttributeValueUpdateRequest request){
        var attributeValue = attributeValueRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_EXISTED));
        attributeValueMapper.updateAttributeValue(attributeValue, request);
        var attribute = attributeRepository.findById(request.getAttributeId())
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_EXISTED));
        attributeValue.setAttribute(attribute);
        return attributeValueMapper.toAttributeValueResponse(attributeValueRepository.save(attributeValue));
    }
}