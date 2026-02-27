package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.request.ProductRequest;
import com.ecommerce.catalog_service.dto.request.ProductUpdateRequest;
import com.ecommerce.catalog_service.dto.response.ProductResponse;
import com.ecommerce.catalog_service.entity.Category;
import com.ecommerce.catalog_service.entity.Product;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.ProductMapper;
import com.ecommerce.catalog_service.repository.BrandRepository;
import com.ecommerce.catalog_service.repository.CategoryRepository;
import com.ecommerce.catalog_service.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest request){
        Product product = productMapper.toProduct(request);

        var brand = brandRepository.findById(request.getBrandId())
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

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public ProductResponse getProduct(String productId){
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> getAllProduct(){
        var products = productRepository.findAll();
        return productMapper.toProductResponseList(products);
    }

    public ProductResponse updateProduct(String productId, ProductUpdateRequest request){
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productMapper.updateProduct(product, request);

        product.setUpdate_at(Instant.now());

        return productMapper.toProductResponse(productRepository.save(product));
    }
}