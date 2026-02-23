package com.ecommerce.identity_service.mapper;

import com.ecommerce.identity_service.dto.request.ProfileCreationRequest;
import com.ecommerce.identity_service.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);

}