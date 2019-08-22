package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.InvalidCurrencyCodeException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrencyTest {

    @Test
    public void givenInvalidCurrencyCode_whenCreate_shouldThrowError() {
        assertThrows(InvalidCurrencyCodeException.class, () -> Currency.of("invalid"));
    }

    @Test
    public void givenValidCurrencyCode_whenCreate_shouldCreateCurrency() {
        var currency = Currency.of("USD");
        assertThat(currency.getValue()).isEqualTo("USD");
    }

}