package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.request.BrandRequest;
import com.ecommerce.catalog_service.dto.request.BrandUpdateRequest;
import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.BrandResponse;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.entity.Brand;
import com.ecommerce.catalog_service.entity.Product;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.BrandMapper;
import com.ecommerce.catalog_service.mapper.ProductMapper;
import com.ecommerce.catalog_service.repository.BrandRepository;
import com.ecommerce.catalog_service.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {
    BrandRepository brandRepository;
    BrandMapper brandMapper;

    public BrandResponse createBrand(BrandRequest request){
        Brand brand = brandMapper.toBrand(request);
        brand.setCreated_at(Instant.now());
        brand.setUpdate_at(Instant.now());

        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }

    public BrandResponse getBrand(String brandId){
        var brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        return brandMapper.toBrandResponse(brand);
    }

    public List<BrandResponse> getAllBrand(){
        var brands = brandRepository.findAll();
        return brandMapper.toBrandResponseList(brands);
    }

    public BrandResponse updateBrand(String brandId, BrandUpdateRequest request){
        var brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        brandMapper.updateBrand(brand, request);
        brand.setUpdate_at(Instant.now());
        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }
}