package com.chougaard.bank.exchange;

import com.chougaard.bank.exchange.dto.ConversionResponse;
import com.chougaard.bank.exchange.dto.ExchangeApiResponse;
import com.chougaard.bank.exchange.dto.HistoricalRatesResponse;
import com.chougaard.bank.exchange.dto.YearlyRate;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ExchangeService {

	private final ExchangeRateClient exchangeRateClient;
	private final String exchangeRateApiKey;
	private final FrankfurterClient frankfurterClient;

	@Inject
	public ExchangeService(@RestClient ExchangeRateClient exchangeRateClient,
						   @ConfigProperty(name = "exchange.api.key") String exchangeRateApiKey,
						   @RestClient FrankfurterClient frankfurterClient) {
		this.exchangeRateClient = exchangeRateClient;
		this.exchangeRateApiKey = exchangeRateApiKey;
		this.frankfurterClient = frankfurterClient;
	}

	public ConversionResponse convertDkkToUsd(BigDecimal amount) {
		ExchangeApiResponse response =
				exchangeRateClient.getPairConversion(exchangeRateApiKey, "DKK", "USD", amount.toPlainString());

		return new ConversionResponse(amount, response.conversionResult());
	}


	public Uni<HistoricalRatesResponse> getHistoricalRates() {
		List<Uni<YearlyRate>> calls = buildQueryDates().stream()
				.map(date -> frankfurterClient.getRateForDate(date, "DKK", "USD")
						.map(list -> new YearlyRate(date, list.getFirst().rate()))
				.onFailure().recoverWithItem(new YearlyRate(date, null)))
				.toList();

		return Uni.combine().all().unis(calls)
				.with(results -> {
					List<YearlyRate> rates = results.stream()
							.map(r -> (YearlyRate) r)
							.toList();
					return new HistoricalRatesResponse(rates);
				});
	}

	public HistoricalRatesResponse getHistoricalRatesSequential() {
		List<YearlyRate> rates = buildQueryDates().stream()
				.map(date -> {
					var list = frankfurterClient.getRateForDate(date, "DKK", "USD")
							.await().indefinitely();
					return new YearlyRate(date, list.getFirst().rate());
				})
				.toList();
		return new HistoricalRatesResponse(rates);
	}

	private List<String> buildQueryDates() {
		List<String> dates = new ArrayList<>();
		for (int year = 2005; year <= 2015; year++) {
			if (year == 2012) continue;
			dates.add(year + "-06-16");
		}
		dates.add(LocalDate.now().toString());
		return dates;
	}
}
