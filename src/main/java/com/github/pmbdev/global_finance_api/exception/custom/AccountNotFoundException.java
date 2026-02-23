package com.github.pmbdev.global_finance_api.exception.custom;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
