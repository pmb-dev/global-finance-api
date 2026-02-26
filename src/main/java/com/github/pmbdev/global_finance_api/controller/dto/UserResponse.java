package com.github.pmbdev.global_finance_api.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
}
