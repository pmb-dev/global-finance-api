package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponse {
    @Schema(example = "1", description = "Internal database ID")
    private Long id;
    @Schema(example = "ES24-1234-5678-9012-3456", description = "International Bank Account Number")
    private String accountNumber;
    @Schema(example = "1250.75", description = "Current balance in the account")
    private BigDecimal balance;
    @Schema(example = "EUR", description = "Currency type")
    private Currency currency;
}
