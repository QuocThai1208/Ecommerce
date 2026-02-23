package com.ecommerce.identity_service.mapper;

import com.ecommerce.identity_service.dto.request.PermissionRequest;
import com.ecommerce.identity_service.dto.response.PermissionResponse;
import com.ecommerce.identity_service.entity.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toPermissionResponse(Permission permission);
    Permission toPermission(PermissionRequest request);
    List<PermissionResponse> toPermissionResponses(List<Permission> permissions);
}