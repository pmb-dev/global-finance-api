package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.controller.dto.AccountResponse;
import com.github.pmbdev.global_finance_api.exception.custom.AccountNotFoundException;
import com.github.pmbdev.global_finance_api.exception.custom.UserNotFoundException;
import com.github.pmbdev.global_finance_api.repository.AccountRepository;
import com.github.pmbdev.global_finance_api.repository.UserRepository;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        // Simulate an authenticated user before the test
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("test@example.com", null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should create account successfully")
    void createAccount_Success() {
        // Arrange
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);

        AccountEntity savedAccount = AccountEntity.builder()
                .id(1L)
                .accountNumber("ES24-1234-5678")
                .balance(BigDecimal.ZERO)
                .currency(Currency.EUR)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(savedAccount);

        // Act
        AccountResponse result = accountService.createAccount(Currency.EUR);

        // Assert
        assertNotNull(result);
        assertEquals(Currency.EUR, result.getCurrency());
        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found in createAccount")
    void createAccount_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            accountService.createAccount(Currency.EUR);
        });
    }

    @Test
    @DisplayName("Should throw exception when account not found in deposit")
    void deposit_NonExistingAccount_ThrowsException() {
        // Arrange
        when(accountRepository.findByAccountNumber("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.deposit("INVALID", BigDecimal.TEN);
        });
    }
}