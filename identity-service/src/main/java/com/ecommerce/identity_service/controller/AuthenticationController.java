package com.ecommerce.identity_service.controller;


import com.ecommerce.identity_service.dto.ApiResponse;
import com.ecommerce.identity_service.dto.request.AuthenticationRequest;
import com.ecommerce.identity_service.dto.request.IntrospectRequest;
import com.ecommerce.identity_service.dto.request.LogoutRequest;
import com.ecommerce.identity_service.dto.request.RefreshRequest;
import com.ecommerce.identity_service.dto.response.AuthenticationResponse;
import com.ecommerce.identity_service.dto.response.IntrospectResponse;
import com.ecommerce.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        AuthenticationResponse  result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest token) throws ParseException, JOSEException {
        authenticationService.logout(token);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest token) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(token);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
}