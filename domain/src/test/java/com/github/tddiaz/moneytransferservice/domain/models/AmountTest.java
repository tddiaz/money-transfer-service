package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.InvalidAmountException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AmountTest {

    private static final Currency USD = Currency.of("USD");

    @Test(expected = InvalidAmountException.class)
    public void givenInvalidValue_whenCreate_shouldThrowError() {
        Amount.of(BigDecimal.ZERO, USD);
    }

    @Test
    public void givenValidValue_whenCreate_shouldCreateAmount() {
        var amount = Amount.of(BigDecimal.ONE, USD);
        assertThat(amount.getValue()).isEqualTo(BigDecimal.ONE);
        assertThat(amount.getCurrency()).isEqualTo("USD");
    }

    @Test
    public void givenAmount_whenSubtract_shouldReturnNewAmount() {
        var amount1 = Amount.of(BigDecimal.TEN, USD);
        var amount2 = Amount.of(BigDecimal.ONE, USD);

        var newAmount = amount1.subtract(amount2);
        assertThat(newAmount.getCurrency()).isEqualTo("USD");
        assertThat(newAmount.getValue()).isEqualTo(BigDecimal.valueOf(9L));
    }

    @Test
    public void givenAmount_whenAdd_shouldReturnNewAmount() {
        var amount1 = Amount.of(BigDecimal.TEN, USD);
        var amount2 = Amount.of(BigDecimal.ONE, USD);

        var newAmount = amount1.add(amount2);
        assertThat(newAmount.getCurrency()).isEqualTo("USD");
        assertThat(newAmount.getValue()).isEqualTo(BigDecimal.valueOf(11L));
    }

    @Test
    public void testIsLessThan() {
        var amount1 = Amount.of(BigDecimal.ONE, USD);
        var amount2 = Amount.of(BigDecimal.valueOf(2L), USD);

        assertTrue(amount1.isLessThan(amount2));
        assertFalse(amount2.isLessThan(amount1));
    }

}