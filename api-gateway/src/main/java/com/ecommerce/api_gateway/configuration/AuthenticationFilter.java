package com.ecommerce.api_gateway.configuration;

import com.ecommerce.api_gateway.dto.ApiResponse;
import com.ecommerce.api_gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] PUBLIC_ENDPOINT = {
            "/identity/auth/.*",
            "/identity/users",
            "/notification/email/send",
            "/file/media/download/.*"
    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public int getOrder() {
        return -1;
    }

    // Kiểm tra các endpoint công khai
    private boolean isPublicEndpoint(ServerHttpRequest request){
        return Arrays.stream(PUBLIC_ENDPOINT)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // kiểm tra endpoint public
        if(isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);
        // lấy token
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        // kiểm tra token rỗng
        if(CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");

        return identityService.introspect(token).flatMap(introspectResponseApiResponse -> {
            if(introspectResponseApiResponse.getResult().isValid()){ // xác thực thành công
                return chain.filter(exchange);
            }
            else{ // xác thực thất bại
                log.info("xác thực thất bại");
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> { // lỗi hệ thống
            log.info("Lỗi hệ thống");
            return unauthenticated(exchange.getResponse());
        });
    }

    // custom response khi chứng thực thất bại
    Mono<Void> unauthenticated(ServerHttpResponse response){
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body = objectMapper.writeValueAsString(apiResponse);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith( // ghi dữ liệu vào card mạng
                Mono.just( // tạo một mono chứa buffer
                        response.bufferFactory().wrap(body.getBytes()) // Đóng gói dữ liệu kiểu byte vào buffer để gửi đi
                )
        );
    }
}