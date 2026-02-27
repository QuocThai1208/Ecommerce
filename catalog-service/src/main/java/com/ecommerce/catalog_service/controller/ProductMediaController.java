package com.ecommerce.catalog_service.controller;


import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.request.ProductMediaRequest;
import com.ecommerce.catalog_service.dto.response.ProductMediaResponse;
import com.ecommerce.catalog_service.service.ProductMediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/product-medias")
public class ProductMediaController {
    ProductMediaService productMediaService;


    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    ApiResponse<ProductMediaResponse> create(
            @RequestPart("file")MultipartFile file,
            @RequestPart("request")String request){
        return ApiResponse.<ProductMediaResponse>builder()
                .message("Upload product media success.")
                .result(productMediaService.create(file, request))
                .build();
    }

    @DeleteMapping("/{productMediaId}")
    ApiResponse<String> delete(@PathVariable String productMediaId){
        return ApiResponse.<String>builder()
                .message("Delete product media success.")
                .result(productMediaService.delete(productMediaId))
                .build();
    }
}