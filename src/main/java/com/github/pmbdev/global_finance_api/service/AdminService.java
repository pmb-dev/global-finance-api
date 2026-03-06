package com.github.pmbdev.global_finance_api.service;

import com.github.pmbdev.global_finance_api.controller.dto.AccountResponse;
import com.github.pmbdev.global_finance_api.controller.dto.UserResponse;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AdminService {
    void blockUser(Long userId);
    List<AccountResponse> getAllAccounts();
    List<UserResponse> getAllUsers();
    Map<Currency, BigDecimal> getTotalBankMoney();
}
