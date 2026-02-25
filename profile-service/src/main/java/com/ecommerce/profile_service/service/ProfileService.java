package com.ecommerce.profile_service.service;

import com.ecommerce.profile_service.dto.request.ProfileCreationRequest;
import com.ecommerce.profile_service.dto.request.ProfileUpdateRequest;
import com.ecommerce.profile_service.dto.response.ProfileResponse;
import com.ecommerce.profile_service.entity.Profile;
import com.ecommerce.profile_service.expention.AppException;
import com.ecommerce.profile_service.expention.ErrorCode;
import com.ecommerce.profile_service.mapper.ProfileMapper;
import com.ecommerce.profile_service.repository.ProfileRepository;
import com.ecommerce.profile_service.repository.httpClient.FileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    FileClient fileClient;

    public ProfileResponse create(ProfileCreationRequest request){
        Profile profile = profileMapper.toProfile(request);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    public ProfileResponse update(ProfileUpdateRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        profileMapper.updateProfile(profile, request);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfileResponse> getAll(){
        return profileMapper.toProfileResponses(profileRepository.findAll());
    }

    public ProfileResponse getProfile(){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Profile profile = profileRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        });
        return profileMapper.toProfileResponse(profile);
    }

    public ProfileResponse getByUserId(String userId){
        Profile profile = profileRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        });
        return profileMapper.toProfileResponse(profile);
    }

    public ProfileResponse updateAvatar(MultipartFile file){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var profile = profileRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        var response = fileClient.uploadAvatar(file);
        var url = response.getResult().getUrl();

        profile.setAvatar(url);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }
}