package com.github.pmbdev.global_finance_api.controller.dto;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryStatResponse implements Serializable{

    private static final long serialVersionUID = 1L;

    @Schema(example = "FOOD", description = "Transaction category")
    private TransactionCategory category;

    @Schema(example = "450.75", description = "Total amount spent in this category")
    private BigDecimal totalAmount;

}