package com.github.pmbdev.global_finance_api.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data // Lombok creates getters and setters
public class LoginRequest {
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;
}