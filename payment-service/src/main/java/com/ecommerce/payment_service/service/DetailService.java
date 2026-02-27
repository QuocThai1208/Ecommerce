package com.ecommerce.payment_service.service;

import com.ecommerce.payment_service.dto.request.TransactionDetailRequest;
import com.ecommerce.payment_service.entity.Transaction;
import com.ecommerce.payment_service.mapper.DetailMapper;
import com.ecommerce.payment_service.repository.DetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Service
public class DetailService {
    DetailRepository detailRepository;
    DetailMapper detailMapper;

    @Transactional
    public void saveAll(Transaction transaction, List<TransactionDetailRequest> detailRequests) {
        var details = detailMapper.toDetailList(detailRequests);
        var now = Instant.now();
        details.forEach(detail ->{
            detail.setTransaction(transaction);
            detail.setCreatedAt(now);
        });
        detailRepository.saveAll(details);
    }
}
