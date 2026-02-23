package com.github.pmbdev.global_finance_api.exception.custom;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
