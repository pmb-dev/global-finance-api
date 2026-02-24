package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private BigDecimal amount;
    private String concept;
    private TransactionCategory category;
}