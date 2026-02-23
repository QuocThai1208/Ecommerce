package com.ecommerce.identity_service.controller;

import com.ecommerce.identity_service.dto.ApiResponse;
import com.ecommerce.identity_service.dto.request.RoleRequest;
import com.ecommerce.identity_service.dto.response.RoleResponse;
import com.ecommerce.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @GetMapping("/{name}")
    ApiResponse<RoleResponse> getRoleDetail(@PathVariable String name){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.getRoleDetail(name))
                .build();
    }

    @PutMapping("/{name}")
    ApiResponse<RoleResponse> update(@PathVariable String name, @RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.update(name, request))
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<String> delete(@PathVariable String roleName){
        return ApiResponse.<String>builder()
                .result(roleService.delete(roleName))
                .build();
    }
}