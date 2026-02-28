package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.controller.dto.CategoryStatResponse;
import com.github.pmbdev.global_finance_api.controller.dto.TransactionResponse;
import com.github.pmbdev.global_finance_api.exception.custom.*;
import com.github.pmbdev.global_finance_api.repository.AccountRepository;
import com.github.pmbdev.global_finance_api.repository.TransactionRepository;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import com.github.pmbdev.global_finance_api.service.TransactionService;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import com.github.pmbdev.global_finance_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ExchangeRateServiceImpl exchangeRateService;

    @Override
    @Transactional // Atomic function
    public TransactionResponse transfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount, String concept, TransactionCategory category) {

        // Check that the source account and the target account are not the same
        if(sourceAccountNumber.equals(targetAccountNumber)){
            throw new SameAccountTransferException("The target account must be different than the source account.");
        }

        // Check that the amount is positive and > 0
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("The amount must be higher than zero.");
        }

        // Check that the category isn't null
        if (category == null) {
            throw new InvalidTransactionDataException("Transaction category is required.");
        }

        // Concept is optional
        String finalConcept = (concept == null || concept.isBlank()) ? "No concept" : concept;

        // Get the email from the token as always
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Search the source Account of the sender
        AccountEntity sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Source account " + sourceAccountNumber + " not found."));

        // Check if the account is from the logged user
        if (!sourceAccount.getUser().getEmail().equals(email)) {
            throw new UnauthorizedAccountAccessException("Account " + sourceAccountNumber + " not owned by sender.");
        }

        // Search the receiver's account by the IBAN
        AccountEntity targetAccount = accountRepository.findByAccountNumber(targetAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Destination account " + targetAccountNumber + " not found."));

        // Check if there´s enough money
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + sourceAccountNumber);
        }

        BigDecimal finalAmount = amount;

        // Different currencies logic
        if (sourceAccount.getCurrency() != targetAccount.getCurrency()) {
            BigDecimal rate = exchangeRateService.getRate(
                    sourceAccount.getCurrency().name(),
                    targetAccount.getCurrency().name()
            );
            finalAmount = amount.multiply(rate);
        }

        // Mathematical operation
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(finalAmount));

        // Save updated amounts
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        // Transaction bank receipt
        TransactionEntity newTransaction = TransactionEntity.builder()
                .amount(amount)
                .targetAmount(finalAmount)
                .timestamp(LocalDateTime.now())
                .sender(sourceAccount)
                .receiver(targetAccount)
                .concept(finalConcept)
                .category(category)
                .build();

        TransactionEntity savedTransaction = transactionRepository.save(newTransaction);

        return TransactionResponse.builder()
                .id(savedTransaction.getId())
                .amount(savedTransaction.getAmount())
                .targetAmount(savedTransaction.getTargetAmount())
                .timestamp(savedTransaction.getTimestamp())
                .concept(savedTransaction.getConcept())
                .category(savedTransaction.getCategory())
                .sourceAccountNumber(savedTransaction.getSender().getAccountNumber())
                .targetAccountNumber(savedTransaction.getReceiver().getAccountNumber())
                .build();
    }

    @Override
    public Page<TransactionResponse> getMyTransactionHistory(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Page<TransactionEntity> transactions = transactionRepository.findAllByUserIdWithFilters(
                currentUser.getId(), startDate, endDate, pageable);

        return transactions.map(tx -> TransactionResponse.builder()
                .id(tx.getId())
                .amount(tx.getAmount())
                .targetAmount(tx.getTargetAmount())
                .timestamp(tx.getTimestamp())
                .concept(tx.getConcept())
                .category(tx.getCategory())
                .sourceAccountNumber(tx.getSender().getAccountNumber())
                .targetAccountNumber(tx.getReceiver().getAccountNumber())
                .build());
    }

    @Override
    public List<CategoryStatResponse> getSpendingStats() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        return transactionRepository.findSpendingByCategory(currentUser.getId());
    }
}