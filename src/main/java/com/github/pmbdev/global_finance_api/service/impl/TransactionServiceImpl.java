package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.repository.AccountRepository;
import com.github.pmbdev.global_finance_api.repository.TransactionRepository;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import com.github.pmbdev.global_finance_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional // Atomic function
    public TransactionEntity transfer(String targetAccountNumber, BigDecimal amount) {

        // Get the email from the token as always
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Search for the sender's account
        // (We are taking the first account the sender has)
        AccountEntity sourceAccount = accountRepository.findAll().stream()
                .filter(acc -> acc.getUser().getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("You do not have any account."));

        // Search the receiver's account by the IBAN
        AccountEntity targetAccount = accountRepository.findByAccountNumber(targetAccountNumber)
                .orElseThrow(() -> new RuntimeException("Destination account not found."));

        // Check if there´s enough money
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds.");
        }

        // Mathematic operation
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        // Save updated amounts
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        // Transaction bank receipt
        TransactionEntity transaction = TransactionEntity.builder()
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .sender(sourceAccount)
                .receiver(targetAccount)
                .build();

        return transactionRepository.save(transaction);
    }
}