package com.ecommerce.identity_service.controller;

import com.ecommerce.identity_service.dto.ApiResponse;
import com.ecommerce.identity_service.dto.request.PermissionRequest;
import com.ecommerce.identity_service.dto.response.PermissionResponse;
import com.ecommerce.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @GetMapping("/{name}")
    ApiResponse<PermissionResponse> getDetail(@PathVariable String name){
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.getPermissionDetail(name))
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<String> delete(@PathVariable String name){
        return ApiResponse.<String>builder()
                .result(permissionService.delete(name))
                .build();
    }
}