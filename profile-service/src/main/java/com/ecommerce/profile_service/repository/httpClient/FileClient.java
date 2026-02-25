package com.ecommerce.profile_service.repository.httpClient;

import com.ecommerce.profile_service.configuration.AuthenticationRequestInterceptor;
import com.ecommerce.profile_service.dto.ApiResponse;
import com.ecommerce.profile_service.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "file-service",
        configuration = {AuthenticationRequestInterceptor.class})
public interface FileClient {
    @PostMapping(value = "/files/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileResponse> uploadAvatar(@RequestPart("file")MultipartFile file);
}