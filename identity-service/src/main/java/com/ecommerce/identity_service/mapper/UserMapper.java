package com.ecommerce.identity_service.mapper;

import com.ecommerce.identity_service.dto.request.UserCreationRequest;
import com.ecommerce.identity_service.dto.request.UserUpdateRequest;
import com.ecommerce.identity_service.dto.response.UserResponse;
import com.ecommerce.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    User toUser(UserCreationRequest request);
    List<UserResponse> toUserResponses(List<User> users);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}