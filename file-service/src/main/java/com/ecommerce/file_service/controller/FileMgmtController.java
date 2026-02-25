package com.ecommerce.file_service.controller;

import com.ecommerce.file_service.dto.ApiResponse;
import com.ecommerce.file_service.dto.response.FileMgmtResponse;
import com.ecommerce.file_service.dto.response.FileResponse;
import com.ecommerce.file_service.service.CloudinaryService;
import com.ecommerce.file_service.service.FileMgmtService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class FileMgmtController {
    CloudinaryService cloudinaryService;
    FileMgmtService fileMgmtService;

    @PostMapping("/media/upload")
    ApiResponse<FileResponse> uploadMedia(@RequestParam("file") MultipartFile file) {
        return ApiResponse.<FileResponse>builder()
                .message("Upload file success.")
                .result(cloudinaryService.uploadFile(file))
                .build();
    }

    @GetMapping("/media")
    ApiResponse<List<FileMgmtResponse>> getAll(){
        return ApiResponse.<List<FileMgmtResponse>>builder()
                .message("Get all list file success.")
                .result(fileMgmtService.getAll())
                .build();
    }
}