package com.github.pmbdev.global_finance_api.exception.custom;

public class InvalidTransactionDataException extends RuntimeException {
    public InvalidTransactionDataException(String message) {
        super(message);
    }
}
