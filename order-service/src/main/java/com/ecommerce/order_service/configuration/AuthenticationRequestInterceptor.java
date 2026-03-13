package com.ecommerce.order_service.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Truy cập vào request gốc(request từ người dùng) để lấy thông tin
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // Lấy token
        assert servletRequestAttributes != null;
        var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");

        // Kiểm tra rỗng, null, chỉ chứa khoản trắng
        if(StringUtils.hasText(authHeader))
            // Áp dụng header cho request tới service khác
            requestTemplate.header("Authorization", authHeader);
    }
}