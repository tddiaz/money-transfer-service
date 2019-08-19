package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.CurrencyMismatchException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.InactiveAccountException;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

public class AccountTest {

    private static final Currency USD = Currency.of("USD");

    @Test
    public void givenValidParams_whenCreate_shouldCreateAccount() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);

        assertThat(account.getNumber().getValue()).isEqualTo("12345678901234");
        assertThat(account.getCurrency().getValue()).isEqualTo("USD");
        assertThat(account.getBalance().getAmount()).isEqualTo(TEN);
        assertThat(account.getBalance().getCurrency()).isEqualTo("USD");
        assertThat(account.getId()).isNotNull();
        assertThat(account.isActive()).isTrue();
    }

    @Test(expected = CurrencyMismatchException.class)
    public void givenAmountWithDifferentCurrency_whenDebit_shouldThrowError() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        var amountToBeDebited = Amount.of(TEN, Currency.of("PHP"));

        account.debit(amountToBeDebited, AccountNumber.of("0000123456890"));
    }

    @Test(expected = InactiveAccountException.class)
    public void givenInactiveAccount_whenDebit_shouldThrowError() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        account.deactivate();

        var amountToBeDebited = Amount.of(TEN, USD);

        account.debit(amountToBeDebited, AccountNumber.of("0000123456890"));
    }

    @Test
    public void givenValidAmountAndActiveAccount_whenDebit_shouldUpdateBalance() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        var amountToBeDebited = Amount.of(TEN, USD);

        account.debit(amountToBeDebited, AccountNumber.of("0000123456890"));

        var expectedUpdatedBalance = Balance.of(Amount.of(ZERO, USD));
        assertThat(account.getBalance()).isEqualTo(expectedUpdatedBalance);
        assertThat(account.getTransactions()).hasSize(1);

        var transaction = account.getTransactions().get(0);
        assertThat(transaction.getAccountId()).isEqualTo(account.getId());
        assertThat(transaction.getType()).isEqualTo(Transaction.Type.DEBIT);
        assertThat(transaction.getAccountNumber()).isEqualTo(AccountNumber.of("0000123456890"));
        assertThat(transaction.getBalance()).isEqualTo(account.getBalance());
    }

    @Test(expected = CurrencyMismatchException.class)
    public void givenAmountWithDifferentCurrency_whenCredit_shouldThrowError() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        var amountToBeCredited = Amount.of(TEN, Currency.of("PHP"));

        account.credit(amountToBeCredited, AccountNumber.of("0000123456890"));
    }

    @Test(expected = InactiveAccountException.class)
    public void givenInactiveAccount_whenCredit_shouldThrowError() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        account.deactivate();

        var amountToBeCredited = Amount.of(TEN, USD);

        account.credit(amountToBeCredited, AccountNumber.of("0000123456890"));
    }

    @Test
    public void givenValidAmountAndActiveAccount_whenCredit_shouldUpdateBalance() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        var amountToBeCredited = Amount.of(TEN, USD);

        account.credit(amountToBeCredited, AccountNumber.of("0000123456890"));

        var expectedUpdatedBalance = Balance.of(Amount.of(BigDecimal.valueOf(20L), USD));
        assertThat(account.getBalance()).isEqualTo(expectedUpdatedBalance);
        assertThat(account.getTransactions()).hasSize(1);

        var transaction = account.getTransactions().get(0);
        assertThat(transaction.getAccountId()).isEqualTo(account.getId());
        assertThat(transaction.getType()).isEqualTo(Transaction.Type.CREDIT);
        assertThat(transaction.getAccountNumber()).isEqualTo(AccountNumber.of("0000123456890"));
        assertThat(transaction.getBalance()).isEqualTo(account.getBalance());
    }

    @Test
    public void givenActiveAccount_whenDeactivate_shouldMarkAccountAsInactive() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        account.deactivate();

        assertFalse(account.isActive());
    }

}