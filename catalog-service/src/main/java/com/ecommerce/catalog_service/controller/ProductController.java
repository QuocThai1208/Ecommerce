package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    @PostMapping
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest request){
        return ApiResponse.<ProductResponse>builder()
                .message("Create product success.")
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ProductResponse>> getAllProduct(){
        return ApiResponse.<List<ProductResponse>>builder()
                .message("Get all product success.")
                .result(productService.getAllProduct())
                .build();
    }

    @GetMapping("/{productId}")
    ApiResponse<ProductResponse> getProduct(@PathVariable String productId){
        return ApiResponse.<ProductResponse>builder()
                .message("Get product success.")
                .result(productService.getProduct(productId))
                .build();
    }

    @PutMapping("/{productId}")
    ApiResponse<ProductResponse> updateProduct(@PathVariable String productId, @RequestBody ProductUpdateRequest request){
        return ApiResponse.<ProductResponse>builder()
                .message("Update product success.")
                .result(productService.updateProduct(productId, request))
                .build();
    }
}