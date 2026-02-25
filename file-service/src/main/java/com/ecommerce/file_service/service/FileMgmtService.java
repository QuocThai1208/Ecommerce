package com.ecommerce.file_service.service;

import com.ecommerce.file_service.dto.response.FileMgmtResponse;
import com.ecommerce.file_service.mapper.FileMgmtMapper;
import com.ecommerce.file_service.repository.FileMgmtRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class FileMgmtService {
    FileMgmtRepository fileMgmtRepository;
    FileMgmtMapper fileMgmtMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<FileMgmtResponse> getAll(){
        return fileMgmtMapper.toFileMgmtResponses(fileMgmtRepository.findAll());
    }
}