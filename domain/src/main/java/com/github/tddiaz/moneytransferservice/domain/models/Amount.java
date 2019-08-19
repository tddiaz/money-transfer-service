package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.utils.Validate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Amount {

    private BigDecimal value;

    @Getter(AccessLevel.NONE)
    private Currency currency;

    public static Amount of(BigDecimal value, Currency currency) {
        Validate.requireNonNull(value, "value cannot be null");
        Validate.requireNonNull(currency, "currency cannot be null");

        return new Amount(value, currency);
    }

    public boolean isLessThan(Amount amount) {
        Validate.requireNonNull(amount, "amount cannot be null");

        return value.compareTo(amount.getValue()) < 0;
    }

    public Amount subtract(Amount amount) {
        Validate.requireNonNull(amount, "amount cannot be null");

        return new Amount(value.subtract(amount.getValue()), currency);
    }

    public Amount add(Amount amount) {
        Validate.requireNonNull(amount, "amount cannot be null");

        return new Amount(value.add(amount.getValue()), currency);
    }

    public String getCurrency() {
        return currency.getValue();
    }
}
