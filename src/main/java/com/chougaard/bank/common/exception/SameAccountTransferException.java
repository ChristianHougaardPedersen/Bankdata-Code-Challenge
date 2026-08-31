package com.chougaard.bank.common.exception;

public class SameAccountTransferException extends ApiServiceException {
	public SameAccountTransferException(String message) {
		super(400, message);
	}
}
