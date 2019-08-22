package com.github.tddiaz.moneytransferservice.domain.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountNumberTest {

    @Test
    void givenNonNullValue_whenCreate_shouldCreateAccountNumber() {
        var accountNumber = AccountNumber.of("12345678901234");
        assertThat(accountNumber.getValue()).isEqualTo("12345678901234");
    }
}