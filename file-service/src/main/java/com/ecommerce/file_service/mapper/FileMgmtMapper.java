package com.ecommerce.file_service.mapper;

import com.ecommerce.file_service.dto.response.FileMgmtResponse;
import com.ecommerce.file_service.entity.FileMgmt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMgmtMapper {
    FileMgmtResponse toFileMgmtResponse(FileMgmt fileMgmt);

    List<FileMgmtResponse> toFileMgmtResponses(List<FileMgmt> fileMgmtList);
}