package com.ecommerce.identity_service.mapper;

import com.ecommerce.identity_service.dto.request.RoleRequest;
import com.ecommerce.identity_service.dto.response.RoleResponse;
import com.ecommerce.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleRequest request);

    List<RoleResponse> toRoleResponses(List<Role> roles);

}