package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.controller.dto.CategoryStatResponse;
import com.github.pmbdev.global_finance_api.controller.dto.TransactionResponse;
import com.github.pmbdev.global_finance_api.controller.dto.TransferRequest;
import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import com.github.pmbdev.global_finance_api.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Manage money transfers, view transaction history or get statistics")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Transfer money",
            description = "Send money to another account with automatic currency conversion if needed.")
    @ApiResponse(responseCode = "200", description = "Transfer successful")
    @ApiResponse(responseCode = "400", description = "Invalid data or insufficient funds")
    @ApiResponse(responseCode = "403", description = "Unauthorized access to source account")
    @ApiResponse(responseCode = "404", description = "Target account not found")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse transaction = transactionService.transfer(
                request.getSourceAccountNumber(),
                request.getTargetAccountNumber(),
                request.getAmount(),
                request.getConcept(),
                request.getCategory()
        );
        return ResponseEntity.ok(transaction);
    }

    @Operation(
            summary = "Get transaction history",
            description = "Retrieve a paginated list of all transactions where the authenticated user is either the sender or the receiver."
    )
    @ApiResponse(responseCode = "200", description = "History retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource")
    @ApiResponse(responseCode = "400", description = "Invalid parameters (e.g., malformed date format)")
    @ApiResponse(responseCode = "403", description = "The account does not belong to the user")
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime endDate
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(transactionService.getMyTransactionHistory(startDate, endDate, pageable));
    }

    @Operation(
            summary = "Get spending statistics",
            description = "Retrieve a list of all expenses made by the user orderer by category."
    )
    @ApiResponse(responseCode = "200", description = "Statistics calculated successfully")
    @ApiResponse(responseCode = "401", description = "Full authentication required")
    @GetMapping("/stats")
    public ResponseEntity<List<CategoryStatResponse>> getStats() {
        return ResponseEntity.ok(transactionService.getSpendingStats());
    }
}