package com.chougaard.bank.common.exception.mapper;

import com.chougaard.bank.common.exception.ApiServiceException;
import com.chougaard.bank.common.exception.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiServiceExceptionMapper implements ExceptionMapper<ApiServiceException> {

	@Override
	public Response toResponse(ApiServiceException exception) {
		return Response.status(
				exception.getStatus())
				.entity(new ErrorResponse(
						exception.getStatus(),
						exception.getMessage()
				)).build();
	}
}
