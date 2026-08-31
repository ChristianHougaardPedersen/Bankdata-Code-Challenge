package com.chougaard.bank.account.dto;

import java.math.BigDecimal;

public record AccountResponse(String accountNumber, String owner, BigDecimal balance) {
}
