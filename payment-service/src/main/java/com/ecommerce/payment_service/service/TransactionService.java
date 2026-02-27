package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.dto.request.CashPaymentSuccessRequest;
import com.ecommerce.payment_service.dto.request.TransactionCreationRequest;
import com.ecommerce.payment_service.dto.request.TransactionDetailRequest;
import com.ecommerce.payment_service.dto.request.TransactionUpdateRequest;
import com.ecommerce.payment_service.dto.response.StripeResponse;
import com.ecommerce.payment_service.dto.response.TransactionResponse;
import com.ecommerce.payment_service.enums.MethodType;
import com.ecommerce.payment_service.enums.TransactionStatus;
import com.ecommerce.payment_service.exception.AppException;
import com.ecommerce.payment_service.exception.ErrorCode;
import com.ecommerce.payment_service.mapper.TransactionMapper;
import com.ecommerce.payment_service.repository.DetailRepository;
import com.ecommerce.payment_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Service
public class TransactionService {
    TransactionRepository transactionRepository;
    DetailRepository detailRepository;

    TransactionMapper transactionMapper;

    StripeService stripeService;
    DetailService detailService;

    // Lưu thông tin thành có (Có thể với nhiều order)
    @Transactional
    public TransactionResponse create(TransactionCreationRequest request) {
        String transactionId = UUID.randomUUID().toString();
        var now = Instant.now();

        var productCheckouts = request.getTransactionDetailRequests().stream()
                .flatMap(detailRequest -> detailRequest.getProducts().stream())
                .collect(Collectors.toSet());

        BigDecimal totalAmount = request.getTransactionDetailRequests().stream()
                .map(TransactionDetailRequest::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var orderIds = request.getTransactionDetailRequests().stream()
                .map(TransactionDetailRequest::getOrderId)
                .toList();

        StripeResponse stripeResponse = stripeService.checkout(productCheckouts, transactionId, orderIds);
        var transaction = transactionMapper.toTransaction(request);

        transaction.setId(transactionId);
        transaction.setTotalAmount(totalAmount);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(now);
        transaction.setUpdateAt(now);
        transaction = transactionRepository.save(transaction);

        detailService.saveAll(transaction, request.getTransactionDetailRequests());

        TransactionResponse response = transactionMapper.toTransactionResponse(transaction);
        response.setSessionUrl(stripeResponse.getSessionUrl());
        return response;
    }

    public String updateStatusSuccess(TransactionUpdateRequest request){
        var transaction = transactionRepository.findByIdAndStatus(request.getTransactionId(), TransactionStatus.PENDING)
                .orElseThrow(()-> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setPaymentIntentId(request.getPaymentIntentId());
        transaction.setIsCapture(true);
        transaction.setUpdateAt(Instant.now());
        transactionRepository.save(transaction);
        return "Cập nhật trạng thái giao dịch thành công.";
    }

    public String updateStatusFailed(TransactionUpdateRequest request){
        var transaction = transactionRepository.findByIdAndStatus(request.getTransactionId(), TransactionStatus.PENDING)
                .orElseThrow(()-> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setUpdateAt(Instant.now());
        transactionRepository.save(transaction);
        return "Cập nhật trạng thái giao dịch thành công.";
    }

    public String cashPaymentSuccess(CashPaymentSuccessRequest request){
        var transactionDetail = detailRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_DETAIL_NOT_FOUND));

        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var transaction = transactionDetail.getTransaction();

        if(!transaction.getUserId().equals(userId) || !transaction.getMethod().equals(MethodType.CASH)) {
            throw new AppException(ErrorCode.CASH_ORDER_ERROR);
        }

        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setIsCapture(true);
        transactionRepository.save(transaction);
        return "Thu tiền thành công.";
    }
}