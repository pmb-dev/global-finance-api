package com.github.pmbdev.global_finance_api.controller.dto;

import lombok.Data;

@Data // Lombok create getters and setters
public class LoginRequest {
    private String email;
    private String password;
}