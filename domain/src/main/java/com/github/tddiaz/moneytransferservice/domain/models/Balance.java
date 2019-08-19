package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.InsufficientFundsException;
import com.github.tddiaz.moneytransferservice.domain.utils.Validate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Balance {

    @Getter(AccessLevel.NONE)
    private Amount amount;

    public static Balance of(Amount amount) {
        Validate.requireNonNull(amount, "amount should not be null");

        return new Balance(amount);
    }

    public Balance deduct(Amount amountToBeDeducted) {
        Validate.requireNonNull(amountToBeDeducted, "amountToBeDeducted should not be null");

        if (this.amount.isLessThan(amountToBeDeducted)) {
            throw new InsufficientFundsException("cannot perform balance deduction. insufficient funds");
        }

        return new Balance(this.amount.subtract(amountToBeDeducted));
    }

    public Balance add(Amount amountToBeAdded) {
        Validate.requireNonNull(amountToBeAdded, "amountToBeAdded should not be null");

        return new Balance(this.amount.add(amountToBeAdded));
    }

    public BigDecimal getAmount() {
        return amount.getValue();
    }

    public String getCurrency() {
        return amount.getCurrency();
    }
}
