package com.ecommerce.file_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.file_service.dto.response.FileResponse;
import com.ecommerce.file_service.entity.FileMgmt;
import com.ecommerce.file_service.expention.AppException;
import com.ecommerce.file_service.expention.ErrorCode;
import com.ecommerce.file_service.repository.FileMgmtRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
}