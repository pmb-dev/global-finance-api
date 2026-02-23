package com.github.pmbdev.global_finance_api.controller.dto;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
import lombok.Data;

@Data
public class AccountRequest {
    private Currency currency;
}
