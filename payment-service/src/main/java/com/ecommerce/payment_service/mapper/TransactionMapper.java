package com.ecommerce.payment_service.mapper;

import com.ecommerce.event.dto.CashPaymentEvent;
import com.ecommerce.payment_service.dto.request.TransactionCreationRequest;
import com.ecommerce.payment_service.dto.response.TransactionResponse;
import com.ecommerce.payment_service.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toTransaction(TransactionCreationRequest request);
    TransactionResponse toTransactionResponse(Transaction transaction);

    Transaction cashPaymentEventToTransaction(CashPaymentEvent cashPaymentEvent);
}