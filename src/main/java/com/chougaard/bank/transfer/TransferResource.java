package com.chougaard.bank.transfer;

import com.chougaard.bank.transfer.dto.TransferRecordResponse;
import com.chougaard.bank.transfer.dto.TransferRequest;
import com.chougaard.bank.transfer.dto.TransferCreatedResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/bank/api/v1/transfers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferResource {

	private final TransferService transferService;

	@Inject
	public TransferResource(TransferService transferService) {
		this.transferService = transferService;
	}

	@POST
	public Response createTransfer(@Valid TransferRequest transferRequest, @Context UriInfo uriInfo) {
		TransferCreatedResponse response = transferService.transfer(transferRequest);
		URI location = uriInfo.getAbsolutePathBuilder()
				.path(String.valueOf(response.transferId()))
				.build();
		return Response.created(location)
				.entity(response)
				.build();
	}

	@GET
	@Path("/{id}")
	public Response getTransfer(@PathParam("id") Long id) {
		TransferRecordResponse response = transferService.getTransfer(id);
		return Response.ok(response).build();
	}
}
