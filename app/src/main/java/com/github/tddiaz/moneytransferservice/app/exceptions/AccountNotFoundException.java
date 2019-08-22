package com.github.tddiaz.moneytransferservice.app.exceptions;

import com.github.tddiaz.moneytransferservice.domain.exceptions.DomainViolationException;

public class AccountNotFoundException extends DomainViolationException {
    private static final long serialVersionUID = -1902827141405157700L;

    public AccountNotFoundException(String message) {
        super(message);
    }
}
