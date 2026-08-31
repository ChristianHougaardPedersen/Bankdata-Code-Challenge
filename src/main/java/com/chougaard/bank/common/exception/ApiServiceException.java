package com.chougaard.bank.common.exception;

public class ApiServiceException extends RuntimeException {

	private final int status;

	protected ApiServiceException(int status, String message) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
