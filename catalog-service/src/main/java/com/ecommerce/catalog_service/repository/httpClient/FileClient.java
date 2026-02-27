package com.ecommerce.catalog_service.repository.httpClient;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.response.FileResponse;
import com.ecommerce.catalog_service.dto.response.MultipleFileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(
        name = "file-service")
public interface FileClient {
    @PostMapping(value = "/files/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileResponse> uploadMedia(@RequestPart("file") MultipartFile file);

    @PostMapping(value = "/media/multiple-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<List<MultipleFileResponse>> uploadMultipleMedia(
            @RequestPart("files") MultipartFile[] files,
            @RequestPart("refIds") List<String> refIds
    );
}