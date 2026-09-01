package com.chougaard.bank.exchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FrankfurterRate(
		String base,
		String date,
		BigDecimal rate
) {
}
