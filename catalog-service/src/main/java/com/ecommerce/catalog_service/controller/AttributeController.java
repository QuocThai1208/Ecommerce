package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.AttributeRequest;
import com.ecommerce.catalog_service.dto.request.AttributeUpdateRequest;
import com.ecommerce.catalog_service.dto.response.AttributeResponse;
import com.ecommerce.catalog_service.service.AttributeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/attributes")
public class AttributeController {
    AttributeService attributeService;

    @PostMapping
    ApiResponse<AttributeResponse> create(@RequestBody AttributeRequest request){
        return ApiResponse.<AttributeResponse>builder()
                .message("Create attribute success.")
                .result(attributeService.createAttribute(request))
                .build();
    }

    @GetMapping("/my-brand")
    ApiResponse<List<AttributeResponse>> getAllByBrand(){
        return ApiResponse.<List<AttributeResponse>>builder()
                .message("Lấy danh sách thuộc tính thành công.")
                .result(attributeService.getByBrandId())
                .build();
    }

    @GetMapping("/{attributeId}")
    ApiResponse<AttributeResponse> getAttribute(@PathVariable String attributeId){
        return ApiResponse.<AttributeResponse>builder()
                .message("Get attribute success.")
                .result(attributeService.getAttribute(attributeId))
                .build();
    }

    @GetMapping
    ApiResponse<List<AttributeResponse>> getAllAttribute(){
        return ApiResponse.<List<AttributeResponse>>builder()
                .message("Get all attribute success.")
                .result(attributeService.getAllAttribute())
                .build();
    }

    @PutMapping("/{attributeId}")
    ApiResponse<AttributeResponse> updateAttribute(@PathVariable String attributeId, @RequestBody AttributeUpdateRequest request){
        return ApiResponse.<AttributeResponse>builder()
                .message("Update attribute success.")
                .result(attributeService.updateAttribute(attributeId, request))
                .build();
    }
}