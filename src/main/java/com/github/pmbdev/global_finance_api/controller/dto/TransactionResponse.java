package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    @Schema(example = "1", description = "Internal database ID")
    private Long id;
    @Schema(example = "50.00", description = "Amount to transfer")
    private BigDecimal amount;
    @Schema(example = "2026-02-26T19:52:17.487Z", description = "Local date and time of the transaction")
    private LocalDateTime timestamp;
    @Schema(example = "Dinner payment", description = "Optional concept for the transfer")
    private String concept;
    @Schema(example = "FOOD", description = "Spending category")
    private TransactionCategory category;
    @Schema(example = "ES24-1111-2222-3333-4444", description = "The IBAN of the sender account")
    private String sourceAccountNumber;
    @Schema(example = "ES24-5555-6666-7777-8888", description = "The IBAN of the receiver account")
    private String targetAccountNumber;
}