package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.InvalidAmountException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Amount {

    private BigDecimal value;

    @Getter(AccessLevel.NONE)
    private Currency currency;

    public static Amount of(@NonNull BigDecimal value, @NonNull Currency currency) {
        if (!isGreaterThanZero(value)) {
            throw new InvalidAmountException("amount value should be greater than zero");
        }

        return new Amount(value, currency);
    }

    public boolean isLessThan(@NonNull Amount amount) {
        return value.compareTo(amount.getValue()) < 0;
    }

    public Amount subtract(@NonNull Amount amount) {
        return new Amount(value.subtract(amount.getValue()), currency);
    }

    public Amount add(@NonNull Amount amount) {
        return new Amount(value.add(amount.getValue()), currency);
    }

    public String getCurrency() {
        return currency.getValue();
    }

    private static boolean isGreaterThanZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }
}
