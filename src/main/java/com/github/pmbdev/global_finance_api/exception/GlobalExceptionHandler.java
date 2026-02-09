package com.github.pmbdev.global_finance_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Listen to all controllers
public class GlobalExceptionHandler {

    // Captures the generic exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage()); // Message of the throw new RuntimeException(...)
        errorResponse.put("status", "400 BAD REQUEST"); // Client's fault (wrong data)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}