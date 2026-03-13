package com.ecommerce.payment_service.mapper;


import com.ecommerce.event.dto.CashPaymentItemEvent;
import com.ecommerce.payment_service.dto.request.TransactionDetailRequest;
import com.ecommerce.payment_service.entity.TransactionDetail;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetailMapper {
    TransactionDetail toDetail(TransactionDetailRequest request);

    List<TransactionDetail> toDetailList(List<TransactionDetailRequest> requests);

    TransactionDetailRequest toTransactionDetailRequest(CashPaymentItemEvent itemEvent);
    List<TransactionDetailRequest> toTransactionDetailRequests(List<CashPaymentItemEvent> itemEvents);
}