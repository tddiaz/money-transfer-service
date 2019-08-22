package com.github.tddiaz.moneytransferservice.domain.models;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountNumber {
    private String value;

    public static AccountNumber of(@NonNull String value) {
        return new AccountNumber(value);
    }
}
