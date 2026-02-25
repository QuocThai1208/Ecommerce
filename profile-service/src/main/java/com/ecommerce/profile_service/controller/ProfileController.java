package com.ecommerce.profile_service.controller;

import com.ecommerce.profile_service.dto.ApiResponse;
import com.ecommerce.profile_service.dto.request.ProfileUpdateRequest;
import com.ecommerce.profile_service.dto.response.ProfileResponse;
import com.ecommerce.profile_service.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @GetMapping("/my-profile")
    ApiResponse<ProfileResponse> getProfile(){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getProfile())
                .build();
    }

    @GetMapping
    ApiResponse<List<ProfileResponse>> getAll(){
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.getAll())
                .build();
    }

    @PutMapping
    ApiResponse<ProfileResponse> updateProfile(@RequestBody ProfileUpdateRequest request){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.update(request))
                .build();
    }

    @PostMapping("/update-avatar")
    ApiResponse<ProfileResponse> updateAvatar(@RequestParam("file")MultipartFile file){
        return ApiResponse.<ProfileResponse>builder()
                .message("Update avatar success.")
                .result(profileService.updateAvatar(file))
                .build();
    }
}