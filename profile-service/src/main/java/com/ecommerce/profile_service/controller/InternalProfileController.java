
package com.ecommerce.profile_service.controller;

import com.ecommerce.profile_service.dto.ApiResponse;
import com.ecommerce.profile_service.dto.request.ProfileCreationRequest;
import com.ecommerce.profile_service.dto.response.ProfileResponse;
import com.ecommerce.profile_service.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProfileController {
    ProfileService profileService;

    @PostMapping("/internal/create")
    ApiResponse<ProfileResponse> create(@RequestBody ProfileCreationRequest request){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.create(request))
                .build();
    }

    @GetMapping("internal/users/{userId}")
    ApiResponse<ProfileResponse> getProfile(@PathVariable String userId){
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getByUserId(userId))
                .build();
    }
}
