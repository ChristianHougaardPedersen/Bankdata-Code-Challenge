package com.chougaard.bank.exchange;

import com.chougaard.bank.exchange.dto.ExchangeApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "exchange-api")
public interface ExchangeRateClient {

	@GET
	@Path("/v6/{apiKey}/pair/{base}/{target}/{amount}")
	ExchangeApiResponse getPairConversion(
			@PathParam("apiKey") String apiKey,
			@PathParam("base") String base,
			@PathParam("target") String target,
			@PathParam("amount") String amount
	);
}
