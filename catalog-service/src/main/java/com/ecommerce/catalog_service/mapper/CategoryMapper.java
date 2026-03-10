package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.CategoryRequest;
import com.ecommerce.catalog_service.dto.request.CategoryUpdateRequest;
import com.ecommerce.catalog_service.dto.response.CategoryResponse;
import com.ecommerce.catalog_service.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parent", ignore = true)
    Category toCategory(CategoryRequest request);

    @Mapping(target = "parentId", source = "parent.id")
    CategoryResponse toResponse(Category category);

    List<CategoryResponse> tCategoryResponseList(List<Category> categoryList);

    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest request);
}