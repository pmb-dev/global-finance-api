package com.github.pmbdev.global_finance_api.exception.custom;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message){
        super(message);
    }
}
