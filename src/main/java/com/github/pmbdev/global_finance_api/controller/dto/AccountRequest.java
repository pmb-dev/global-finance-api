package com.github.pmbdev.global_finance_api.controller.dto;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AccountRequest {
    @Schema(example = "EUR", description = "Currency type")
    @NotNull(message = "Currency is required.")
    private Currency currency;
}
