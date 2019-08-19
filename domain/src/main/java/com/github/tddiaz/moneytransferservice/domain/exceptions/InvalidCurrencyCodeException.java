package com.github.tddiaz.moneytransferservice.domain.exceptions;

public class InvalidCurrencyCodeException extends DomainViolationException {
    private static final long serialVersionUID = 6757453637409219496L;

    public InvalidCurrencyCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
