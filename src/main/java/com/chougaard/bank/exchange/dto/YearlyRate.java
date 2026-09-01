package com.chougaard.bank.exchange.dto;

import java.math.BigDecimal;

public record YearlyRate(String date, BigDecimal rate) {
}
