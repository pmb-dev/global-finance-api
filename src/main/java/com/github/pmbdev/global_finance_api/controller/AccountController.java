package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.controller.dto.AccountRequest;
import com.github.pmbdev.global_finance_api.controller.dto.AccountResponse;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Accounts", description = "Operations related to bank accounts, balances, and deposits")
public class AccountController {

    private final AccountService accountService;

    @Operation(
            summary = "Create Account",
            description = "Create a new account for the user.")
    @ApiResponse(responseCode = "201", description = "Account created successfully")
    @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource")
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) { // We don't need to request because we already know the user
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(request.getCurrency()));
    }

    @Operation(
            summary = "Get accounts",
            description = "Retrieve a list of all the accounts and their details owned by the user."
    )
    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource")
    @ApiResponse(responseCode = "400", description = "Invalid parameters")
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

    @Operation(
            summary = "Deposit money",
            description = "Deposit money in an account.")
    @ApiResponse(responseCode = "200", description = "Amount added successfully")
    @ApiResponse(responseCode = "400", description = "Invalid amount")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@Valid @RequestBody DepositRequest request) {
        accountService.deposit(request.getAccountNumber(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}