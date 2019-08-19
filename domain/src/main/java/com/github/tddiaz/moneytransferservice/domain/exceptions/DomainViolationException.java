package com.github.tddiaz.moneytransferservice.domain.exceptions;

public class DomainViolationException extends RuntimeException {
    private static final long serialVersionUID = 8129016381990968598L;

    public DomainViolationException(String message) {
        super(message);
    }

    public DomainViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
