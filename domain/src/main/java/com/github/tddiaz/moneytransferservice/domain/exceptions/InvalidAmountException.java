package com.github.tddiaz.moneytransferservice.domain.exceptions;

public class InvalidAmountException extends DomainViolationException {
    private static final long serialVersionUID = -4399780549734337155L;

    public InvalidAmountException(String message) {
        super(message);
    }
}
