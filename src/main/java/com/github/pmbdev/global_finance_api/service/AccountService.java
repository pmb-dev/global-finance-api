package com.github.pmbdev.global_finance_api.service;

import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import java.util.List;

public interface AccountService {
    AccountEntity createAccount();
    List<AccountEntity> getMyAccounts();
}
