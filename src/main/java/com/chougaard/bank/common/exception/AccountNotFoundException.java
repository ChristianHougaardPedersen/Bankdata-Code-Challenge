package com.chougaard.bank.common.exception;

public class AccountNotFoundException extends ApiServiceException {
    public AccountNotFoundException(String message) {
        super(404, message);
    }
}
