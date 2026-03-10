package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ProductDetailResponse;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.dto.response.VariantInflowResponse;
import com.ecommerce.catalog_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    @PostMapping
    ApiResponse<ProductResponse> createProduct(@RequestPart("file") MultipartFile[] files,
                                               @RequestPart("request")String request){
        return ApiResponse.<ProductResponse>builder()
                .message("Create product success.")
                .result(productService.createProduct(files, request))
                .build();
    }

    @GetMapping("/my-product")
    ApiResponse<List<ProductResponse>> getAllProductByBrandId(){
        return ApiResponse.<List<ProductResponse>>builder()
                .message("Lấy danh sách sản phẩm thành công.")
                .result(productService.getAllProductByBrandId())
                .build();
    }

    @GetMapping("/{productId}")
    ApiResponse<ProductDetailResponse> getProduct(@PathVariable String productId){
        return ApiResponse.<ProductDetailResponse>builder()
                .message("Lấy thông tin sản phẩm thành công.")
                .result(productService.getProduct(productId))
                .build();
    }

    @GetMapping("/{productId}/variants")
    ApiResponse<List<VariantInflowResponse>> getVariantsByProductId(@PathVariable String productId){
        return ApiResponse.<List<VariantInflowResponse>>builder()
                .message("Lấy danh sách phân loại thành công.")
                .result(productService.getVariantByProductId(productId))
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