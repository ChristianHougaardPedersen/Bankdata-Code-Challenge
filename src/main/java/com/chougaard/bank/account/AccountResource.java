package com.chougaard.bank.account;

import com.chougaard.bank.account.dto.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/bank/api/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    private final AccountService accountService;

    @Inject
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    public Response createAccount(@Valid CreateAccountRequest request, @Context UriInfo uriInfo) {
        AccountResponse account = accountService.createAccount(request);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(account.accountNumber())
                .build();
        return Response.created(location)
                .entity(account)
                .build();
    }

    @GET
    @Path("/{accountNumber}")
    public Response getAccountByAccountNumber(@PathParam("accountNumber") String accountNumber) {
        AccountResponse response = accountService.getAccountByAccountNumber(accountNumber);
        return Response.ok(response).build();
    }

    @POST
    @Path("/{accountNumber}/deposits")
    public Response deposit(@PathParam("accountNumber") String accountNumber, @Valid DepositRequest depositRequest) {
        AccountResponse account = accountService.deposit(accountNumber, depositRequest);
        return Response.ok(account).build();
    }

}
