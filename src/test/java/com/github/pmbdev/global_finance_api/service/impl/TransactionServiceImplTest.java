package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.exception.custom.AccountNotFoundException;
import com.github.pmbdev.global_finance_api.repository.UserRepository;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.controller.dto.TransferRequest;
import com.github.pmbdev.global_finance_api.exception.custom.InsufficientFundsException;
import com.github.pmbdev.global_finance_api.repository.AccountRepository;
import com.github.pmbdev.global_finance_api.repository.TransactionRepository;
import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Activate Mockito
class TransactionServiceImplTest {

    @Mock // Create the "actors"
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExchangeRateServiceImpl exchangeRateService;

    @Mock
    private UserRepository userRepository;

    private MeterRegistry meterRegistry;

    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();

        transactionService = new TransactionServiceImpl(
                transactionRepository,
                accountRepository,
                userRepository,
                exchangeRateService,
                meterRegistry
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("test@example.com", null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        // Clean security context after the test
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should throw exception when balance is insufficient")
    void transfer_InsufficientFunds_ThrowsException() {
        // 1. ARRANGE
        // Create owner
        UserEntity owner = new UserEntity();
        owner.setEmail("test@example.com");

        // Create source account
        AccountEntity source = new AccountEntity();
        source.setAccountNumber("ES123");
        source.setBalance(new BigDecimal("50.00"));
        source.setUser(owner);

        // Create target account
        AccountEntity target = new AccountEntity();
        target.setAccountNumber("ES456");

        // Simulate a transfer request
        TransferRequest request = new TransferRequest();
        request.setSourceAccountNumber("ES123");
        request.setTargetAccountNumber("ES456");
        request.setAmount(new BigDecimal("100.00")); // Try to send more money than owned
        request.setConcept("Test transfer");
        request.setCategory(TransactionCategory.OTHERS);

        // Mocks: Configurate account behaviours
        when(accountRepository.findByAccountNumber("ES123"))
                .thenReturn(Optional.of(source));

        when(accountRepository.findByAccountNumber("ES456"))
                .thenReturn(Optional.of(target));

        // 2. ACT & 3. ASSERT
        assertThrows(InsufficientFundsException.class, () -> {
            transactionService.transfer(
                    request.getSourceAccountNumber(),
                    request.getTargetAccountNumber(),
                    request.getAmount(),
                    request.getConcept(),
                    request.getCategory()
            );
        });
    }

    @Test
    @DisplayName("Should successfully transfer between accounts")
    void transfer_SufficientBalance_Success() {
        // Arrange

        UserEntity owner = new UserEntity();
        owner.setEmail("test@example.com");

        AccountEntity source = new AccountEntity();
        source.setAccountNumber("ES123");
        source.setBalance(new BigDecimal("1000.00"));
        source.setCurrency(Currency.EUR);
        source.setUser(owner);

        AccountEntity target = new AccountEntity();
        target.setAccountNumber("ES456");
        target.setBalance(new BigDecimal("500.00"));
        target.setCurrency(Currency.EUR);

        when(accountRepository.findByAccountNumber("ES123"))
                .thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumber("ES456"))
                .thenReturn(Optional.of(target));
        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        // Act
        transactionService.transfer("ES123", "ES456",
                new BigDecimal("200.00"), "Test", TransactionCategory.TRANSFER);

        // Assert
        assertEquals(new BigDecimal("800.00"), source.getBalance());
        assertEquals(new BigDecimal("700.00"), target.getBalance());
    }

    @Test
    @DisplayName("Should throw exception when source account not found")
    void transfer_SourceNotFound_ThrowsException() {
        when(accountRepository.findByAccountNumber("INVALID"))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.transfer("INVALID", "ES456",
                    new BigDecimal("100"), "Test", TransactionCategory.TRANSFER);
        });
    }
}