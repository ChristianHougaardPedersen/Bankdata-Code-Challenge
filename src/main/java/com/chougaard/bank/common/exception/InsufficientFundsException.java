package com.chougaard.bank.common.exception;

public class InsufficientFundsException extends ApiServiceException {
	public InsufficientFundsException(String message) {
		super(409, message);
	}
}
