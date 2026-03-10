package com.ecommerce.identity_service.service;

import com.ecommerce.event.dto.NotificationEvent;
import com.ecommerce.identity_service.dto.request.UserCreationRequest;
import com.ecommerce.identity_service.dto.request.UserRoleUpdateRequest;
import com.ecommerce.identity_service.dto.request.UserUpdateRequest;
import com.ecommerce.identity_service.dto.response.EmailResponse;
import com.ecommerce.identity_service.dto.response.UserResponse;
import com.ecommerce.identity_service.entity.Role;
import com.ecommerce.identity_service.exception.AppException;
import com.ecommerce.identity_service.exception.ErrorCode;
import com.ecommerce.identity_service.mapper.ProfileMapper;
import com.ecommerce.identity_service.mapper.UserMapper;
import com.ecommerce.identity_service.repository.RoleRepository;
import com.ecommerce.identity_service.repository.UserRepository;
import com.ecommerce.identity_service.repository.httpClient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    ProfileMapper profileMapper;
    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    AuthenticationService authenticationService;

    public EmailResponse getMyEmail(){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return EmailResponse.builder()
                .email(user.getEmail())
                .build();
    }

    public UserResponse createUser(UserCreationRequest request) {
        String otpSave = authenticationService.getOtp(request.getEmail());
        if(otpSave == null || !otpSave.equals(request.getOtp())){
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        var user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById("USER").ifPresent(roles::add);

        user.setRoles(roles);
        user = userRepository.save(user);

        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());

        var profileResponse = profileClient.createProfile(profileRequest);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("email")
                .recipient(request.getEmail())
                .subject("Welcome to E-Commerce")
                .body("Hello " + request.getUsername())
                .build();

        // public message to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);
        return userMapper.toUserResponse(user);
    }

    public UserResponse getCurrentUser() {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(UserUpdateRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse assignRolesToUser(String userId, UserRoleUpdateRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        var users = userRepository.findAll();
        return userMapper.toUserResponses(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            return "User not found";
        }
        userRepository.deleteById(userId);
        return "User deleted successfully";
    }

}