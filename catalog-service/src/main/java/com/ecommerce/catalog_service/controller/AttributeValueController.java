package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.AttributeValueRequest;
import com.ecommerce.catalog_service.dto.request.AttributeValueUpdateRequest;
import com.ecommerce.catalog_service.dto.response.AttributeValueResponse;
import com.ecommerce.catalog_service.service.AttributeValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/attribute-values")
public class AttributeValueController {
    AttributeValueService attributeValueService;

    @PostMapping
    ApiResponse<AttributeValueResponse> create(@RequestBody AttributeValueRequest request){
        return ApiResponse.<AttributeValueResponse>builder()
                .message("Create attribute value success.")
                .result(attributeValueService.createAttributeValue(request))
                .build();
    }

    @GetMapping("/{attributeValueId}")
    ApiResponse<AttributeValueResponse> getAttributeValue(@PathVariable String attributeValueId){
        return ApiResponse.<AttributeValueResponse>builder()
                .message("Get attribute success.")
                .result(attributeValueService.getAttributeValue(attributeValueId))
                .build();
    }

    @GetMapping
    ApiResponse<List<AttributeValueResponse>> getAllAttributeValue(){
        return ApiResponse.<List<AttributeValueResponse>>builder()
                .message("Get all attribute success.")
                .result(attributeValueService.getAllAttributeValue())
                .build();
    }

    @PutMapping("/{attributeValueId}")
    ApiResponse<AttributeValueResponse> updateAttributeValue(@PathVariable String attributeValueId, @RequestBody AttributeValueUpdateRequest request){
        return ApiResponse.<AttributeValueResponse>builder()
                .message("Update attribute success.")
                .result(attributeValueService.updateAttributeValue(attributeValueId, request))
                .build();
    }
}