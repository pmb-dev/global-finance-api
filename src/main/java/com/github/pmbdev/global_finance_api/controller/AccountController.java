package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountEntity> createAccount() { // We don't need to request because we already know the user
        AccountEntity newAccount = accountService.createAccount();

        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }

    @GetMapping
    public ResponseEntity<List<AccountEntity>> getMyAccounts() {
        List<AccountEntity> accounts = accountService.getMyAccounts();
        return ResponseEntity.ok(accounts);
    }
}