package com.github.pmbdev.global_finance_api.exception.custom;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}