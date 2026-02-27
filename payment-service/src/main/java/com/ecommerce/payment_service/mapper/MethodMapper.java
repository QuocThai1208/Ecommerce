package com.ecommerce.payment_service.mapper;

import com.ecommerce.payment_service.dto.request.MethodCreationRequest;
import com.ecommerce.payment_service.dto.response.MethodResponse;
import com.ecommerce.payment_service.entity.Method;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MethodMapper {
    Method toMethod(MethodCreationRequest request);
    MethodResponse toResponse(Method method);
    List<MethodResponse> toMethodResponseList(List<Method> methods);
}