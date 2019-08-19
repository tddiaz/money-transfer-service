package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.utils.Validate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountNumber {
    private String value;

    public static AccountNumber of(String value) {
        Validate.requireNonNull(value, "value should not be null");

        return new AccountNumber(value);
    }
}
