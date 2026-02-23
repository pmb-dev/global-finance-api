package com.github.pmbdev.global_finance_api.service;

import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    AccountEntity createAccount(Currency currency);
    List<AccountEntity> getMyAccounts();
    void deposit(String accountNumber, BigDecimal amount);

}
