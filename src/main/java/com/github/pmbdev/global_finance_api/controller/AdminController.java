package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.controller.dto.AccountResponse;
import com.github.pmbdev.global_finance_api.controller.dto.UserResponse;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
import com.github.pmbdev.global_finance_api.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Operations", description = "Endpoints restricted to users with ROLE_ADMIN")
public class AdminController {
    private final AdminService adminService;

    @Operation(
            summary = "Get accounts",
            description = "Obtain all the generated accounts by all users.")
    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource")
    @ApiResponse(responseCode = "403", description = "Permission denied: Admin role required")
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(adminService.getAllAccounts());
    }

    @Operation(
            summary = "Block user",
            description = "Block an user from the bank.")
    @ApiResponse(responseCode = "204", description = "User blocked successfully")
    @ApiResponse(responseCode = "403", description = "Permission denied")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PatchMapping("/users/{id}/block")
    public ResponseEntity<Void> blockUser(
            @Parameter(description = "The unique ID of the user to block", example = "15")
            @PathVariable Long id) {
        adminService.blockUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all users",
            description = "List all registered users in the platform.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource")
    @ApiResponse(responseCode = "403", description = "Permission denied: Admin role required")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @Operation(
            summary = "Get bank total balance",
            description = "Returns the sum of all money in all accounts.")
    @ApiResponse(responseCode = "200", description = "Total balance retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource")
    @ApiResponse(responseCode = "403", description = "Permission denied: Admin role required")
    @GetMapping("/stats/total-balance")
    public ResponseEntity<Map<Currency, BigDecimal>> getTotalBankMoney() {
        return ResponseEntity.ok(adminService.getTotalBankMoney());
    }
}
