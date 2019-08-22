package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.InvalidCurrencyCodeException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Currency {
    private String value;

    public static Currency of(@NonNull String value) {

        try {
            java.util.Currency.getInstance(value);
        } catch (Exception e) {
            throw new InvalidCurrencyCodeException("invalid currency code", e);
        }

        return new Currency(value);
    }

}
