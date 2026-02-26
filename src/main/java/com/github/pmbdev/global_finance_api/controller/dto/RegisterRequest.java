package com.github.pmbdev.global_finance_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data   // Lombok creates getters and setters
public class RegisterRequest {
    @Schema(example = "user@example.com", description = "Email owned by user to register in the database")
    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Invalid email format.")
    private String email;

    @Schema(example = "password123", description = "Strong password to access the API")
    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
