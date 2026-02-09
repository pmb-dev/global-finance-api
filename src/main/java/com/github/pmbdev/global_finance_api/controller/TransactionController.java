package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.controller.dto.TransferRequest;
import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import com.github.pmbdev.global_finance_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionEntity> transfer(@RequestBody TransferRequest request) {
        TransactionEntity transaction = transactionService.transfer(
                request.getSourceAccountNumber(),
                request.getTargetAccountNumber(),
                request.getAmount()
        );
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionEntity>> getHistory(){
        return ResponseEntity.ok(transactionService.getMyTransactionHistory());
    }
}