package com.github.pmbdev.global_finance_api.controller.dto;

import lombok.Data;

@Data   // Lombok creates getters and setters
public class RegisterRequest {
    private String email;
    private String password;
}
