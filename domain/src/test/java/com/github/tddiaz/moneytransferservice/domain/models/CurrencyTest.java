package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.InvalidCurrencyCodeException;
import com.github.tddiaz.moneytransferservice.domain.models.Currency;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyTest {

    @Test(expected = InvalidCurrencyCodeException.class)
    public void givenInvalidCurrencyCode_whenCreate_shouldThrowError() {
        Currency.of("invalid");
    }

    @Test
    public void givenValidCurrencyCode_whenCreate_shouldCreateCurrency() {
        var currency = Currency.of("USD");
        assertThat(currency.getValue()).isEqualTo("USD");
    }

}