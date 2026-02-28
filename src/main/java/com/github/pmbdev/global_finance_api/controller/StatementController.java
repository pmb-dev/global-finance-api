package com.github.pmbdev.global_finance_api.controller;

import com.github.pmbdev.global_finance_api.service.impl.StatementServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operations related to bank accounts, balances, and statements")
public class StatementController {

    private final StatementServiceImpl statementService;

    @Operation(
            summary = "Download Account Statement",
            description = "Generates a PDF document with the account details and recent transactions.")
    @ApiResponse(responseCode = "200", description = "PDF generated and downloaded successfully")
    @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource")
    @ApiResponse(responseCode = "403", description = "You are not authorized to access this account's statement")
    @ApiResponse(responseCode = "404", description = "Account number not found")
    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<byte[]> downloadStatement(@PathVariable String accountNumber) {
        byte[] pdfContent = statementService.generateAccountStatement(accountNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Specific name
        headers.setContentDispositionFormData("attachment", "statement-" + accountNumber + ".pdf");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}
