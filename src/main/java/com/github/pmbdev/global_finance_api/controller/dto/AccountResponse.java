package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;
}
