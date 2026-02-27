package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.ProductMediaRequest;
import com.ecommerce.catalog_service.dto.response.ProductMediaResponse;
import com.ecommerce.catalog_service.entity.ProductMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMediaMapper {
    @Mapping(target = "product", ignore = true)
    ProductMedia toProductMedia(ProductMediaRequest request);

    ProductMediaResponse toProductMediaResponse(ProductMedia productMedia);

    List<ProductMediaResponse> toProductMediaResponseList(List<ProductMedia> productMediaList);
}