package com.ecommerce.file_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.file_service.dto.response.FileResponse;
import com.ecommerce.file_service.dto.response.MultipleFileResponse;
import com.ecommerce.file_service.entity.FileMgmt;
import com.ecommerce.file_service.exception.AppException;
import com.ecommerce.file_service.exception.ErrorCode;
import com.ecommerce.file_service.repository.FileMgmtRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CloudinaryService {
    Cloudinary cloudinary;
    FileMgmtRepository fileMgmtRepository;

    @SuppressWarnings("rawtype") // Tắt cảnh báo khi khai báo Map không có kiểu dữ liệu
    public FileResponse uploadFile(MultipartFile file){
        try{
            // Gửi file lên cloudinary
            Map response = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            // Build fileMgmt
            String contentType = response.get("resource_type").toString() + "/" + response.get("format");
            FileMgmt fileMgmt = FileMgmt.builder()
                    .id(response.get("public_id").toString())
                    .contentType(contentType)
                    .url(response.get("url").toString())
                    .version(response.get("version").toString())
                    .size(response.get("bytes").toString())
                    .build();

            fileMgmt = fileMgmtRepository.save(fileMgmt);

            // Return fileResponse
            return FileResponse.builder()
                    .originalFileName(fileMgmt.getId())
                    .url(fileMgmt.getUrl())
                    .build();
        }catch(IOException e){
            throw new AppException(ErrorCode.FILE_UPLOAD_FAIL);
        }
    }

    @Transactional
    public List<MultipleFileResponse> uploadMultipleFile(MultipartFile[] files, List<String> refIds) {
        if (files.length != refIds.size()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        List<MultipleFileResponse> responses = new ArrayList<>();
        List<FileMgmt> fileMgmtToSave = new ArrayList<>();
        for(int i = 0; i< files.length; i++){
            try{
                MultipartFile file = files[i];
                String currentRefId = refIds.get(i);

                // Gửi file lên cloudinary
                Map response = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                // Build fileMgmt
                String contentType = response.get("resource_type").toString() + "/" + response.get("format");
                FileMgmt fileMgmt = FileMgmt.builder()
                        .id(response.get("public_id").toString())
                        .contentType(contentType)
                        .url(response.get("url").toString())
                        .version(response.get("version").toString())
                        .size(response.get("bytes").toString())
                        .build();

                fileMgmtToSave.add(fileMgmt);

                responses.add(MultipleFileResponse.builder()
                                .originalFileName(fileMgmt.getId())
                                .refId(currentRefId)
                                .url(fileMgmt.getUrl())
                        .build());
            }catch(IOException e){
                throw new AppException(ErrorCode.FILE_UPLOAD_FAIL);
            }
        }

        fileMgmtRepository.saveAll(fileMgmtToSave);
        return responses;
    }
}