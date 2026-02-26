package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.controller.dto.CategoryStatResponse;
import com.github.pmbdev.global_finance_api.controller.dto.TransactionResponse;
import com.github.pmbdev.global_finance_api.controller.dto.TransferRequest;
import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import com.github.pmbdev.global_finance_api.service.TransactionService;
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
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransferRequest request) {
        TransactionResponse transaction = transactionService.transfer(
                request.getSourceAccountNumber(),
                request.getTargetAccountNumber(),
                request.getAmount(),
                request.getConcept(),
                request.getCategory()
        );
        return ResponseEntity.ok(transaction);
    }

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

    @GetMapping("/stats")
    public ResponseEntity<List<CategoryStatResponse>> getStats() {
        return ResponseEntity.ok(transactionService.getSpendingStats());
    }
}