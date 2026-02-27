package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.CategoryRequest;
import com.ecommerce.catalog_service.dto.request.CategoryUpdateRequest;
import com.ecommerce.catalog_service.dto.response.CategoryResponse;
import com.ecommerce.catalog_service.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/categories")
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    ApiResponse<CategoryResponse> create(@RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .message("Create Category success.")
                .result(categoryService.create(request))
                .build();
    }

    @GetMapping("/{categoryId}")
    ApiResponse<CategoryResponse> getCategory(@PathVariable String categoryId){
        return ApiResponse.<CategoryResponse>builder()
                .message("Create Category success.")
                .result(categoryService.getCategory(categoryId))
                .build();
    }

    @GetMapping
    ApiResponse<List<CategoryResponse>> getAllCategory(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Create Category success.")
                .result(categoryService.getAllCategory())
                .build();
    }

    @PutMapping("/{categoryId}")
    ApiResponse<CategoryResponse> update(
            @PathVariable String categoryId,
            @RequestBody CategoryUpdateRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .message("Create Category success.")
                .result(categoryService.update(categoryId, request))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    ApiResponse<String> delete(@PathVariable String categoryId){
        return ApiResponse.<String>builder()
                .message("Create Category success.")
                .result(categoryService.delete(categoryId))
                .build();
    }
}