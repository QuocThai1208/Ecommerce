package com.ecommerce.identity_service.controller;

import com.ecommerce.identity_service.dto.ApiResponse;
import com.ecommerce.identity_service.dto.request.UserCreationRequest;
import com.ecommerce.identity_service.dto.request.UserRoleUpdateRequest;
import com.ecommerce.identity_service.dto.request.UserUpdateRequest;
import com.ecommerce.identity_service.dto.response.UserResponse;
import com.ecommerce.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping("/me")
    ApiResponse<UserResponse> getCurrentUser(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getCurrentUser())
                .build();
    }

    @PutMapping("/me")
    ApiResponse<UserResponse> updateCurrentUser(@RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(request))
                .build();
    }

    @PutMapping("/{userId}/roles")
    ApiResponse<UserResponse> assignRolesToUser(@PathVariable String userId, @RequestBody UserRoleUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.assignRolesToUser(userId, request))
                .build();
    }


    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }


    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        return ApiResponse.<String>builder()
                .result(userService.deleteUser(userId))
                .build();
    }

}