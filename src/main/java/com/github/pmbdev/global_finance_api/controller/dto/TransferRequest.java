package com.github.pmbdev.global_finance_api.controller.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private BigDecimal amount;
}