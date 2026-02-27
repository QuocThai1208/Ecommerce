package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.request.CategoryRequest;
import com.ecommerce.catalog_service.dto.request.CategoryUpdateRequest;
import com.ecommerce.catalog_service.dto.response.CategoryResponse;
import com.ecommerce.catalog_service.entity.Category;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.CategoryMapper;
import com.ecommerce.catalog_service.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse create(CategoryRequest request){
        Category category = categoryMapper.toCategory(request);

        if(StringUtils.hasText(request.getParentId())){
            var parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

            category.setParent(parent);
        }

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public CategoryResponse getCategory(String categoryId){
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return categoryMapper.toResponse(category);
    }

    public List<CategoryResponse> getAllCategory(){
        var categories = categoryRepository.findAll();
        return categoryMapper.tCategoryResponseList(categories);
    }

    public CategoryResponse update(String categoryId, CategoryUpdateRequest request){
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        categoryMapper.updateCategory(category, request);

        if(StringUtils.hasText(request.getParentId())){
            var parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

            category.setParent(parent);
        }

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public String delete(String categoryId){
        if (!categoryRepository.existsById(categoryId)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }

        categoryRepository.deleteById(categoryId);
        return "Delete category success.";
    }
}