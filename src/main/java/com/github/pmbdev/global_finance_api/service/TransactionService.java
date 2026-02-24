package com.github.pmbdev.global_finance_api.service;

import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionEntity transfer(
            String sourceAccountNumber,
            String targetAccountNumber,
            BigDecimal amount,
            String concept,
            TransactionCategory category);
    Page<TransactionEntity> getMyTransactionHistory(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
