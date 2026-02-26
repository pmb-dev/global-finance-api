package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    @Schema(example = "ES24-1111-2222-3333-4444", description = "The IBAN of the sender account")
    @NotBlank(message = "Source account is required.")
    private String sourceAccountNumber;

    @Schema(example = "ES24-5555-6666-7777-8888", description = "The IBAN of the receiver account")
    @NotBlank(message = "Target account is required.")
    private String targetAccountNumber;

    @Schema(example = "50.00", description = "Amount to transfer")
    @NotNull(message = "Amount is required.")
    @Positive(message = "Amount must be greater than zero.")
    private BigDecimal amount;

    @Schema(example = "Dinner payment", description = "Optional concept for the transfer")
    private String concept;

    @Schema(example = "FOOD", description = "Spending category")
    @NotNull(message = "Category is required.")
    private TransactionCategory category;
}