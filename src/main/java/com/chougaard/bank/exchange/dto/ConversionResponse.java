package com.chougaard.bank.exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ConversionResponse(
		@JsonProperty("DKK") BigDecimal dkk,
		@JsonProperty("USD") BigDecimal usd
		) {
}
