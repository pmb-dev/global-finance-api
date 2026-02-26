package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String concept;
    private TransactionCategory category;
    private String sourceAccountNumber;
    private String targetAccountNumber;
}