package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public interface CategoryStatResponse {
    @Schema(example = "FOOD", description = "Transaction category")
    TransactionCategory getCategory();
    @Schema(example = "450.75", description = "Total amount spent in this category")
    BigDecimal getTotalAmount();
}