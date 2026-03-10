package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.request.ProductMediaRequest;
import com.ecommerce.catalog_service.dto.response.MultipleFileResponse;
import com.ecommerce.catalog_service.dto.response.ProductMediaResponse;
import com.ecommerce.catalog_service.entity.Product;
import com.ecommerce.catalog_service.entity.ProductMedia;
import com.ecommerce.catalog_service.exception.AppException;
import com.ecommerce.catalog_service.exception.ErrorCode;
import com.ecommerce.catalog_service.mapper.ProductMediaMapper;
import com.ecommerce.catalog_service.repository.ProductMediaRepository;
import com.ecommerce.catalog_service.repository.ProductRepository;
import com.ecommerce.catalog_service.repository.httpClient.FileClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductMediaService {
    ProductMediaRepository productMediaRepository;
    ProductRepository productRepository;

    ProductMediaMapper productMediaMapper;

    FileClient fileClient;

    public ProductMediaResponse create(MultipartFile file, String requestJson){
        var now = Instant.now();
        ProductMediaRequest request;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            request = objectMapper.readValue(requestJson, ProductMediaRequest.class);
        }catch(Exception e){
            throw new RuntimeException("Error parsing JSON request: ", e);
        }
        var productMedia = productMediaMapper.toProductMedia(request);

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        productMedia.setProduct(product);

        try{
            var response = fileClient.uploadMedia(file);
            productMedia.setMediaUrl(response.getResult().getUrl());
        }catch (Exception e){
            throw new RuntimeException("Upload media error: ",e);
        }
        productMedia.setIsMain(!productMediaRepository.existsByProductSlugAndIsMainTrue(request.getProductId()));

        productMedia.setSortOrder(productMediaRepository.findMaxSortOrderByProductId(request.getProductId()) + 1);
        productMedia.setCreated_at(now);
        productMedia.setUpdate_at(now);
        return productMediaMapper.toProductMediaResponse(productMediaRepository.save(productMedia));
    }

    @Transactional
    public void uploadMultipleFile(MultipartFile[] files, Product product){
        var now = Instant.now();

        List<String> refIds = Arrays.stream(files)
                .map(MultipartFile::getOriginalFilename)
                .toList();
        boolean hasMainImage = productMediaRepository.existsByProductSlugAndIsMainTrue(product.getSlug());
        Integer currentMaxSortOrder = productMediaRepository.findMaxSortOrderByProductId(product.getSlug());
        List<ProductMedia> mediaToSave = new ArrayList<>();

        try{
            var uploadResponse = fileClient.uploadMultipleMedia(files, refIds);
            List<MultipleFileResponse> fileDataList = uploadResponse.getResult();

            for(int i = 0; i<fileDataList.size(); i++){
                var fileData = fileDataList.get(i);

                ProductMedia media = ProductMedia.builder()
                        .product(product)
                        .mediaUrl(fileData.getUrl())
                        .sortOrder(currentMaxSortOrder + i + 1)
                        .created_at(now)
                        .update_at(now)
                        .build();

                if (!hasMainImage && i == 0) {
                    media.setIsMain(true);
                    hasMainImage = true; // Đánh dấu để các ảnh sau trong vòng lặp là false
                } else {
                    media.setIsMain(false);
                }
                mediaToSave.add(media);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        productMediaRepository.saveAll(mediaToSave);
    }

    public String delete(String productMediaId){
        productMediaRepository.findById(productMediaId)
                .orElseThrow(() ->  new AppException(ErrorCode.PRODUCT_MEDIA_NOT_EXISTED));
        productMediaRepository.deleteById(productMediaId);
        return "Delete product media success.";
    }
}