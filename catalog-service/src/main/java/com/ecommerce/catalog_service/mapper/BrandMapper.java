package com.ecommerce.catalog_service.mapper;

import com.ecommerce.catalog_service.dto.request.BrandRequest;
import com.ecommerce.catalog_service.dto.request.BrandUpdateRequest;
import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.BrandResponse;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.entity.Brand;
import com.ecommerce.catalog_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandRequest request);

    BrandResponse toBrandResponse(Brand brand);

    List<BrandResponse> toBrandResponseList(List<Brand> brandList);

    void updateBrand(@MappingTarget Brand brand, BrandUpdateRequest request);
}