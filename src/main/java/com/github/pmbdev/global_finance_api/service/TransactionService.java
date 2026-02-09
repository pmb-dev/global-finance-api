package com.github.pmbdev.global_finance_api.service;

import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    TransactionEntity transfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount);
    List<TransactionEntity> getMyTransactionHistory();
}
