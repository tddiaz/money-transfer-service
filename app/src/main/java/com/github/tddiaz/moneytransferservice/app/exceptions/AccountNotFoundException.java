package com.github.tddiaz.moneytransferservice.app.exceptions;

public class AccountNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -1902827141405157700L;

    public AccountNotFoundException(String message) {
        super(message);
    }
}
