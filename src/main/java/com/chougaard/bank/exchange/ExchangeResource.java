package com.chougaard.bank.exchange;

import com.chougaard.bank.exchange.dto.ConversionResponse;
import com.chougaard.bank.exchange.dto.HistoricalRatesResponse;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.math.BigDecimal;

@Path("/bank/api/v1/exchange")
@Produces(MediaType.APPLICATION_JSON)
public class ExchangeResource {

	private final ExchangeService service;

	@Inject
	public ExchangeResource(ExchangeService service) {
		this.service = service;
	}

	@GET
	@Path("/dkk-usd")
	public ConversionResponse convert(@QueryParam("amount") @DefaultValue("100") BigDecimal amount) {
		return service.convertDkkToUsd(amount);
	}

	@GET
	@Path("/dkk-usd/historical")
	public Uni<HistoricalRatesResponse> historical() {
		return service.getHistoricalRates();
	}

	@GET
	@Path("/dkk-usd/historical-sequential")
	public HistoricalRatesResponse historicalSequential() {
		return service.getHistoricalRatesSequential();
	}
}
