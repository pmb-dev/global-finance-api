package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.controller.dto.AccountResponse;
import com.github.pmbdev.global_finance_api.controller.dto.UserResponse;
import com.github.pmbdev.global_finance_api.exception.custom.UserNotFoundException;
import com.github.pmbdev.global_finance_api.repository.AccountRepository;
import com.github.pmbdev.global_finance_api.repository.UserRepository;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
import com.github.pmbdev.global_finance_api.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void blockUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        userRepository.updateEnabledStatus(userId, false);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(acc -> AccountResponse.builder()
                        .accountNumber(acc.getAccountNumber())
                        .balance(acc.getBalance())
                        .currency(acc.getCurrency())
                        .id(acc.getId())
                        .build())
                .toList();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .toList();
    }

    @Override
    public Map<Currency, BigDecimal> getTotalBankMoney() {
        List<Object[]> results = accountRepository.findTotalBalancesByCurrency();
        Map<Currency, BigDecimal> totals = new HashMap<>();

        for (Object[] result : results) {
            totals.put((Currency) result[0], (BigDecimal) result[1]);
        }
        return totals;
    }
}