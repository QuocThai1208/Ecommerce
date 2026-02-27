package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.BrandRequest;
import com.ecommerce.catalog_service.dto.request.BrandUpdateRequest;
import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.BrandResponse;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.service.BrandService;
import com.ecommerce.catalog_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/brands")
public class BrandController {
    BrandService brandService;

    @PostMapping
    ApiResponse<BrandResponse> create(@RequestBody BrandRequest request){
        return ApiResponse.<BrandResponse>builder()
                .message("Create brand success.")
                .result(brandService.createBrand(request))
                .build();
    }

    @GetMapping("/{brandId}")
    ApiResponse<BrandResponse> getBrand(@PathVariable String brandId){
        return ApiResponse.<BrandResponse>builder()
                .message("Get brand success.")
                .result(brandService.getBrand(brandId))
                .build();
    }

    @GetMapping
    ApiResponse<List<BrandResponse>> getAllBrand(){
        return ApiResponse.<List<BrandResponse>>builder()
                .message("Get all brand success.")
                .result(brandService.getAllBrand())
                .build();
    }

    @PutMapping("/{brandId}")
    ApiResponse<BrandResponse> updateBrand(@PathVariable String brandId, @RequestBody BrandUpdateRequest request){
        return ApiResponse.<BrandResponse>builder()
                .message("Update brand success.")
                .result(brandService.updateBrand(brandId, request))
                .build();
    }
}