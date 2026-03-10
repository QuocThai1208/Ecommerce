package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.BrandRequest;
import com.ecommerce.catalog_service.dto.request.BrandUpdateRequest;
import com.ecommerce.catalog_service.dto.response.BrandResponse;
import com.ecommerce.catalog_service.dto.response.EmailResponse;
import com.ecommerce.catalog_service.entity.Brand;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.BrandMapper;
import com.ecommerce.catalog_service.repository.BrandRepository;
import com.ecommerce.catalog_service.repository.httpClient.IdentityClient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
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

    IdentityClient identityClient;

    public BrandResponse createBrand(BrandRequest request){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var now = Instant.now();

        Brand brand = brandMapper.toBrand(request);
        brand.setUserId(userId);
        brand.setCreated_at(now);
        brand.setUpdate_at(now);

        try{
            ApiResponse<EmailResponse> response = identityClient.getMyEmail();
            brand.setEmail(response.getResult().getEmail());
        }catch(Exception e){
            throw new AppException(ErrorCode.ERROR_AT_IDENTITY_SERVICE);
        }
        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }

    public BrandResponse getBrandByUserId(){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var brand = brandRepository.findByUserId(userId)
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