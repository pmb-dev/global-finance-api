package com.github.pmbdev.global_finance_api.exception.custom;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
