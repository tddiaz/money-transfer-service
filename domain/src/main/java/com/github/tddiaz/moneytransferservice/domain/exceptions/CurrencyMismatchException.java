package com.github.tddiaz.moneytransferservice.domain.exceptions;

public class CurrencyMismatchException extends DomainViolationException {
    private static final long serialVersionUID = -4468776206817061227L;

    public CurrencyMismatchException(String message) {
        super(message);
    }
}
