package com.github.tddiaz.moneytransferservice.domain.exceptions;

public class InactiveAccountException extends DomainViolationException {
    private static final long serialVersionUID = 4293926287960642465L;

    public InactiveAccountException(String message) {
        super(message);
    }
}
