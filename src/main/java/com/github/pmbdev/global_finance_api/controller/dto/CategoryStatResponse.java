package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import java.math.BigDecimal;

public interface CategoryStatResponse {
    TransactionCategory getCategory();
    BigDecimal getTotalAmount();
}