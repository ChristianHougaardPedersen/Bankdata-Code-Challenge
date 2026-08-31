package com.chougaard.bank.common.exception;

public class TransferNotFoundException extends ApiServiceException {
	public TransferNotFoundException(String message) {
		super(404, message);
	}
}
