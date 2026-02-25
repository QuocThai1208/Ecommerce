package com.ecommerce.profile_service.mapper;

import com.ecommerce.profile_service.dto.request.ProfileCreationRequest;
import com.ecommerce.profile_service.dto.request.ProfileUpdateRequest;
import com.ecommerce.profile_service.dto.response.ProfileResponse;
import com.ecommerce.profile_service.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileCreationRequest request);
    ProfileResponse toProfileResponse(Profile profile);

    List<ProfileResponse> toProfileResponses(List<Profile> profiles);

    void updateProfile(@MappingTarget Profile profile, ProfileUpdateRequest request);
}