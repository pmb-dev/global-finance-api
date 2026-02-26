package com.github.pmbdev.global_finance_api.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data   // Lombok creates getters and setters
public class RegisterRequest {
    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
