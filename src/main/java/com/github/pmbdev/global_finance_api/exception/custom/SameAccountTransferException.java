package com.github.pmbdev.global_finance_api.exception.custom;

public class SameAccountTransferException extends RuntimeException {
    public SameAccountTransferException(String message) {
        super(message);
    }
}
