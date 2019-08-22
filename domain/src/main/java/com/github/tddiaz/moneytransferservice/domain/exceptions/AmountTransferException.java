package com.github.tddiaz.moneytransferservice.domain.exceptions;

public class AmountTransferException extends DomainViolationException {
    private static final long serialVersionUID = 9211302369779989967L;

    public AmountTransferException(String message) {
        super(message);
    }

    public AmountTransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
