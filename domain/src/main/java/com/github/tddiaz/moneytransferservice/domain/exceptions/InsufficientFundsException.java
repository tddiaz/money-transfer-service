package com.github.tddiaz.moneytransferservice.domain.exceptions;

public class InsufficientFundsException extends DomainViolationException {
    private static final long serialVersionUID = 7144172274816979355L;

    public InsufficientFundsException(String message) {
        super(message);
    }
}
