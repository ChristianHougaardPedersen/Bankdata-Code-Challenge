package com.chougaard.bank.exchange;

import com.chougaard.bank.exchange.dto.ConversionResponse;
import com.chougaard.bank.exchange.dto.ExchangeApiResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;

@ApplicationScoped
public class ExchangeService {

	@RestClient
	@Inject
	private final ExchangeRateClient client;
	private final String apiKey;

	public ExchangeService(@RestClient ExchangeRateClient client,
						   @ConfigProperty(name = "exchange.api.key") String apiKey) {
		this.client = client;
		this.apiKey = apiKey;
	}

	public ConversionResponse convertDkkToUsd(BigDecimal amount) {
		ExchangeApiResponse response =
				client.getPairConversion(apiKey, "DKK", "USD", amount.toPlainString());

		return new ConversionResponse(amount, response.conversionResult());
	}
}
