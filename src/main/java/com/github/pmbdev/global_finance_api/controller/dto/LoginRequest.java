package com.github.pmbdev.global_finance_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data // Lombok creates getters and setters
public class LoginRequest {
    @Schema(example = "user@example.com", description = "Registered user email")
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @Schema(example = "password123", description = "User password")
    @NotBlank(message = "Password is required.")
    private String password;
}