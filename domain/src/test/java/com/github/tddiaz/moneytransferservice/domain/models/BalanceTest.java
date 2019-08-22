package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BalanceTest {

    private static final Currency USD = Currency.of("USD");

    @Test
    void givenValidAmount_whenCreate_shouldCreateNewBalance() {
        var balance = Balance.of(Amount.of(BigDecimal.TEN, USD));
        assertThat(balance.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(balance.getCurrency()).isEqualTo("USD");
    }

    @Test
    void givenLargerAmount_whenDeduct_shouldThrowError() {
        var balance = Balance.of(Amount.of(BigDecimal.TEN, USD));
        assertThrows(InsufficientFundsException.class, () -> balance.deduct(Amount.of(BigDecimal.valueOf(11L), USD)));
    }

    @Test
    void givenValidAmount_whenDeduct_shouldReturnNewBalance() {
        var balance = Balance.of(Amount.of(BigDecimal.TEN, USD));
        var newBalance = balance.deduct(Amount.of(BigDecimal.TEN, USD));
        assertThat(newBalance.getAmount()).isEqualTo(BigDecimal.ZERO);
        assertThat(newBalance.getCurrency()).isEqualTo("USD");
    }

    @Test
    void givenValidAmount_whenAdd_shouldReturnNewBalance() {
        var balance = Balance.of(Amount.of(BigDecimal.TEN, USD));
        var newBalance = balance.add(Amount.of(BigDecimal.TEN, USD));
        assertThat(newBalance.getAmount()).isEqualTo(BigDecimal.valueOf(20L));
        assertThat(newBalance.getCurrency()).isEqualTo("USD");
    }

}