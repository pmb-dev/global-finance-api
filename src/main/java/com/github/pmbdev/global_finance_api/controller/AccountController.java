package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.controller.dto.AccountRequest;
import com.github.pmbdev.global_finance_api.controller.dto.AccountResponse;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.service.AccountService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) { // We don't need to request because we already know the user
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(request.getCurrency()));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getMyAccounts() {
        List<AccountResponse> accounts = accountService.getMyAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Data
    public static class DepositRequest {
        private String accountNumber;
        private BigDecimal amount;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody DepositRequest request) {
        accountService.deposit(request.getAccountNumber(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}