package com.chougaard.bank.transfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(@NotBlank String fromAccountNumber, @NotBlank String toAccountNumber, @NotNull @DecimalMin("0.01")
                              BigDecimal amount) {
}
