package com.ecommerce.identity_service.service;

import com.ecommerce.identity_service.dto.request.RoleRequest;
import com.ecommerce.identity_service.dto.response.RoleResponse;
import com.ecommerce.identity_service.entity.Role;
import com.ecommerce.identity_service.exception.AppException;
import com.ecommerce.identity_service.exception.ErrorCode;
import com.ecommerce.identity_service.mapper.RoleMapper;
import com.ecommerce.identity_service.repository.PermissionRepository;
import com.ecommerce.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll() {
        return roleMapper.toRoleResponses(roleRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse getRoleDetail(String name) {
        var role = roleRepository.findById(name).orElseThrow(() -> {
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        });
        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String delete(String name) {
        if (!roleRepository.existsById(name)) throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        roleRepository.deleteById(name);
        return "Delete role success.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse update(String name, RoleRequest request) {
        var role = roleRepository.findById(name).orElseThrow(() -> {
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        });

        roleMapper.updateRole(role, request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }
}