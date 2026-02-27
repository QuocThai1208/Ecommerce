package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.dto.request.MethodCreationRequest;
import com.ecommerce.payment_service.dto.response.MethodResponse;
import com.ecommerce.payment_service.exception.AppException;
import com.ecommerce.payment_service.exception.ErrorCode;
import com.ecommerce.payment_service.mapper.MethodMapper;
import com.ecommerce.payment_service.repository.MethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class MethodService {
    MethodRepository methodRepository;

    MethodMapper methodMapper;

    public MethodResponse create(MethodCreationRequest request){
        var method = methodMapper.toMethod(request);
        var methodIsDefaultTrue = methodRepository.findByUserIdAndIsDefaultTrue(request.getUserId())
                .orElse(null);
        if (methodIsDefaultTrue == null){
            method.setIsDefault(true);
        }
        method.setCreatedAt(Instant.now());
        return methodMapper.toResponse(methodRepository.save(method));
    }

    @Transactional
    public MethodResponse choiceIsDefault(String methodId){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var methodIsDefaultTrueExist = methodRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElse(null);
        if(methodIsDefaultTrueExist == null || !methodIsDefaultTrueExist.getId().equals(methodId)){
            if (methodIsDefaultTrueExist != null){
                methodIsDefaultTrueExist.setIsDefault(false);
                methodRepository.save(methodIsDefaultTrueExist);
            }
            var method = methodRepository.findById(methodId).orElseThrow(
                    () -> new AppException(ErrorCode.METHOD_NOT_FOUND));
            method.setIsDefault(true);
            return methodMapper.toResponse(methodRepository.save(method));
        }
        return methodMapper.toResponse(methodIsDefaultTrueExist);
    }

    public String inactive(String methodId){
        var method = methodRepository.findByIdAndIsActiveTrue(methodId)
                .orElse(null);
        if(method == null) return "Phương thức không tồn tại hoặc đã bị vô hiệu hóa.";
        method.setIsActive(false);
        methodRepository.save(method);
        return "Vô hiệu hóa phương thức thành công.";
    }

    public MethodResponse getMethod(String methodId){
        var method = methodRepository.findById(methodId).orElseThrow(
                () -> new AppException(ErrorCode.METHOD_NOT_FOUND));
        return methodMapper.toResponse(method);
    }

    public List<MethodResponse> getMyMethods(){
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var methods = methodRepository.findAllByUserId(userId);
        return methodMapper.toMethodResponseList(methods);
    }
}