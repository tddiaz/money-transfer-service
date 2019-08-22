package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.InsufficientFundsException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Balance {

    @Getter(AccessLevel.NONE)
    private Amount amount;

    public static Balance of(@NonNull Amount amount) {
        return new Balance(amount);
    }

    public Balance deduct(@NonNull Amount amountToBeDeducted) {
        if (this.amount.isLessThan(amountToBeDeducted)) {
            throw new InsufficientFundsException("cannot perform balance deduction. insufficient funds");
        }

        return new Balance(this.amount.subtract(amountToBeDeducted));
    }

    public Balance add(@NonNull Amount amountToBeAdded) {
        return new Balance(this.amount.add(amountToBeAdded));
    }

    public BigDecimal getAmount() {
        return amount.getValue();
    }

    public String getCurrency() {
        return amount.getCurrency();
    }
}
