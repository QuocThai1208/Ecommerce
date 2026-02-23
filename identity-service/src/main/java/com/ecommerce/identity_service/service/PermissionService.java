package com.ecommerce.identity_service.service;

import com.ecommerce.identity_service.dto.request.PermissionRequest;
import com.ecommerce.identity_service.dto.response.PermissionResponse;
import com.ecommerce.identity_service.entity.Permission;
import com.ecommerce.identity_service.expention.AppException;
import com.ecommerce.identity_service.expention.ErrorCode;
import com.ecommerce.identity_service.mapper.PermissionMapper;
import com.ecommerce.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse getPermissionDetail(String name){
        Permission permission = permissionRepository.findById(name).orElseThrow(() -> {
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        });
        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionResponse> getAll(){
        return permissionMapper.toPermissionResponses(permissionRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String delete(String name){
        if(!permissionRepository.existsById(name)) throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        permissionRepository.deleteById(name);
        return "Delete role success.";
    }
}