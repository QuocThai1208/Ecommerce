package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.ItemBatchDetailRequest;
import com.ecommerce.catalog_service.dto.request.ProductVariantRequest;
import com.ecommerce.catalog_service.dto.request.ProductVariantUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ItemBatchDetailResponse;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.dto.response.ProductVariantResponse;
import com.ecommerce.catalog_service.enums.ProductStatus;
import com.ecommerce.catalog_service.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/product-variants")
public class ProductVariantController {
    ProductVariantService productVariantService;

    @PostMapping("/batch-details")
    ApiResponse<List<ItemBatchDetailResponse>> getBatchDetails(@RequestBody ItemBatchDetailRequest request){
        return ApiResponse.<List<ItemBatchDetailResponse>>builder()
                .message("Lấy giá và tên lô hàng thành công.")
                .result(productVariantService.getBatchDetails(request))
                .build();
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    ApiResponse<List<ProductVariantResponse>> create(
            @RequestPart("files") MultipartFile[] files,
            @RequestPart("request") List<ProductVariantRequest> request
    ){
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .message("Create product variant success.")
                .result(productVariantService.createProductVariant(request, files))
                .build();
    }

    @GetMapping("/{productVariantId}")
    ApiResponse<ProductVariantResponse> getProductVariant(@PathVariable String productVariantId){
        return ApiResponse.<ProductVariantResponse>builder()
                .message("Get product variant success.")
                .result(productVariantService.getProductVariant(productVariantId))
                .build();
    }

    @GetMapping
    ApiResponse<List<ProductVariantResponse>> getAllProductVariant(){
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .message("Get all product variant success.")
                .result(productVariantService.getAllProductVariant())
                .build();
    }

    @PutMapping("/{productVariantId}")
    ApiResponse<ProductVariantResponse> updateProductVariant(
            @PathVariable String productVariantId,
            @RequestBody ProductVariantUpdateRequest request){
        return ApiResponse.<ProductVariantResponse>builder()
                .message("Get product variant success.")
                .result(productVariantService.updateProductVariant(productVariantId, request))
                .build();
    }

    @PutMapping("/{productVariantId}/update-visibility")
    ApiResponse<ProductStatus> updateVisibility(@PathVariable String productVariantId, @RequestParam("action") String action){
        return ApiResponse.<ProductStatus>builder()
                .message("Cập nhật status thành công.")
                .result(productVariantService.updateVisibility(productVariantId, action))
                .build();
    }
}