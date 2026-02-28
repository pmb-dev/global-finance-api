package com.github.pmbdev.global_finance_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    @Schema(example = "1", description = "Unique user identifier")
    private Long id;
    @Schema(example = "John Doe", description = "Full name of the user")
    private String name;
    @Schema(example = "user@example.com", description = "User email address")
    private String email;
}
