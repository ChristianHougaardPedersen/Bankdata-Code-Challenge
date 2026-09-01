package com.chougaard.bank.exchange;

import com.chougaard.bank.exchange.dto.FrankfurterRate;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "frankfurter-api")
public interface FrankfurterClient {

	@GET
	@Path("/v2/rates")
	Uni<List<FrankfurterRate>> getRateForDate(
			@QueryParam("date") String date,
			@QueryParam("base") String base,
			@QueryParam("quotes") String quotes
	);

}
