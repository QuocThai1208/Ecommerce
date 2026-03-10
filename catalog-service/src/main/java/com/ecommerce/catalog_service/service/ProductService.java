package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.request.ProductMediaRequest;
import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ProductDetailResponse;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.dto.response.ProductVariantResponse;
import com.ecommerce.catalog_service.dto.response.VariantInflowResponse;
import com.ecommerce.catalog_service.entity.Category;
import com.ecommerce.catalog_service.entity.Product;
import com.ecommerce.catalog_service.entity.ProductMedia;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.ProductMapper;
import com.ecommerce.catalog_service.mapper.ProductVariantMapper;
import com.ecommerce.catalog_service.repository.*;
import com.ecommerce.catalog_service.repository.httpClient.InventoryClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    BrandRepository brandRepository;
    CategoryRepository categoryRepository;
    ProductMediaRepository mediaRepository;
    ProductVariantRepository variantRepository;

    ProductMapper productMapper;
    ProductVariantMapper variantMapper;

    ProductMediaService mediaService;
    ProductVariantService variantService;

    InventoryClient inventoryClient;

    @Transactional
    public ProductResponse createProduct(MultipartFile[] files, String requestJson){
        ProductRequest request;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            request = objectMapper.readValue(requestJson, ProductRequest.class);
        }catch(Exception e){
            throw new RuntimeException("Error parsing JSON request: ", e);
        }
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Product product = productMapper.toProduct(request);

        var brand = brandRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        Iterable<Category> foundCategory = categoryRepository.findAllById(request.getCategories());
        Set<Category> categorySet = StreamSupport.stream(
                foundCategory.spliterator(),
                false
        ).collect(Collectors.toSet());

        product.setBrand(brand);
        product.setCategories(categorySet);
        product.setUpdate_at(Instant.now());
        product.setCreated_at(Instant.now());

        var response =  productMapper.toProductResponse(productRepository.save(product));

        if(files.length != 0) mediaService.uploadMultipleFile(files, product);

        response.setCategories(categorySet.stream()
                .map(Category::getName)
                .collect(Collectors.toSet()));
        return response;
    }

    public ProductDetailResponse getProduct(String productId){
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        var medias = mediaRepository.findAllByProductSlug(product.getSlug());

        var response =  productMapper.toProductDetailResponse(product);

        response.setCategories(product.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet()));

        response.setImages(medias.stream()
                .map(ProductMedia::getMediaUrl)
                .collect(Collectors.toSet()));

        response.setVariants(variantService.getByProductSlug(product.getSlug()));

        Set<String> variantIds = response.getVariants().stream()
                .map(ProductVariantResponse::getSku)
                .collect(Collectors.toSet());

        try{
            var res = inventoryClient.getMyWarehouse(product.getBrand().getId());
            response.setWarehouses(res.getResult());
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        try{
            var res = inventoryClient.getByVariantIds(variantIds);
            response.setInventories(res.getResult());
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        return response;
    }

    public List<ProductResponse> getAllProductByBrandId(){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var brand = brandRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        var products = productRepository.findAllByBrandId(brand.getId());
        return products.stream()
                .map(product -> {
                    var productResponse = productMapper.toProductResponse(product);
                    productResponse.setCategories(product.getCategories().stream()
                            .map(Category::getName)
                            .collect(Collectors.toSet()));

                    var media = mediaRepository.findByProductSlugAndIsMainTrue(product.getSlug())
                            .orElse(null);
                    productResponse.setMainImage(media != null ? media.getMediaUrl() : null);
                    return productResponse;
                }).toList();
    }

    public ProductResponse updateProduct(String productId, ProductUpdateRequest request){
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productMapper.updateProduct(product, request);

        product.setUpdate_at(Instant.now());

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<VariantInflowResponse> getVariantByProductId(String productId){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var brand = brandRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        var product = productRepository.findBySlugAndBrandId(productId, brand.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        var variants = variantRepository.findAllByProductSlug(product.getSlug());
        return variantMapper.toVariantInflowResponseList(variants);
    }
}