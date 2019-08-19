package com.github.tddiaz.moneytransferservice.domain.models;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountId {
    private String value;

    public static AccountId create() {
        return new AccountId(UUID.randomUUID().toString());
    }
}
