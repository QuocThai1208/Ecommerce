package com.ecommerce.catalog_service.repository.httpClient;

import com.ecommerce.catalog_service.configuration.AuthenticationRequestInterceptor;
import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.response.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "identity-service",
        configuration = {AuthenticationRequestInterceptor.class}
)
public interface IdentityClient {
    @GetMapping("/identity/internal/my-email")
    ApiResponse<EmailResponse> getMyEmail();
}
