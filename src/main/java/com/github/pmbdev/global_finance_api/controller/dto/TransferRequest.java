package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotBlank(message = "Source account is required.")
    private String sourceAccountNumber;

    @NotBlank(message = "Target account is required.")
    private String targetAccountNumber;

    @NotNull(message = "Amount is required.")
    @Positive(message = "Amount must be greater than zero.")
    private BigDecimal amount;

    private String concept;

    @NotNull(message = "Category is required.")
    private TransactionCategory category;
}