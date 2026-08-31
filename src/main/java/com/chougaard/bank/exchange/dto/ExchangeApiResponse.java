package com.chougaard.bank.exchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeApiResponse(
		String result,
		@JsonProperty("conversion_rate") BigDecimal conversionRate,
		@JsonProperty("conversion_result") BigDecimal conversionResult
		) {
}
