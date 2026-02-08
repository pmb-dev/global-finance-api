package com.github.pmbdev.global_finance_api.service.impl;

import com.github.pmbdev.global_finance_api.repository.AccountRepository;
import com.github.pmbdev.global_finance_api.repository.UserRepository;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import com.github.pmbdev.global_finance_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    // We need the account object and the user object to know who's creating it
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountEntity createAccount() {
        // We get the authenticated user and get the name
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // We search for the user in the database
        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Create the new account
        AccountEntity newAccount = AccountEntity.builder()
                .accountNumber(generateIban()) // Random number
                .balance(BigDecimal.ZERO) // Initial balance = 0
                .user(currentUser) // Assign the user
                .build();

        // Save in DB
        return accountRepository.save(newAccount);
    }

    @Override
    public List<AccountEntity> getMyAccounts() {
        // Get user by token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return accountRepository.findByUser(currentUser);
    }

    // Creating the IBAN
    private String generateIban() {
        StringBuilder s = new StringBuilder();
        for(int i=0; i<5; i++){
            int num = (int)(Math.random() * 9000) + 1000;
            s.append(num);
            if(i<4){
                s.append("-");
            }
        }
        return "ES24-" + s;
    }

    @Override
    public void deposit(String accountNumber, BigDecimal amount) {
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found."));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

}