package com.chougaard.bank.exchange;

import com.chougaard.bank.exchange.dto.ConversionResponse;
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
}
