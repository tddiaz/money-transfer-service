package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.CurrencyMismatchException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.DomainViolationException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.InactiveAccountException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    @Test(expected = DomainViolationException.class)
    public void givenSameAccountNumber_whenDebit_shouldThrowError() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        var amountToBeDebited = Amount.of(TEN, Currency.of("PHP"));

        account.debit(amountToBeDebited, AccountNumber.of("12345678901234"));
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

        assertThat(account.getBalance().getAmount()).isEqualTo(ZERO);
        assertThat(account.getBalance().getCurrency()).isEqualTo(USD.getValue());
        assertThat(account.getTransactions()).hasSize(1);

        var transaction = account.getTransactions().get(0);
        assertThat(transaction.getAccountId()).isEqualTo(account.getId());
        assertThat(transaction.getAmount()).isEqualTo(amountToBeDebited);
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.DEBIT);
        assertThat(transaction.getAccountNumber()).isEqualTo(AccountNumber.of("0000123456890"));
        assertThat(transaction.getBalance()).isEqualTo(account.getBalance());
    }

    @Test(expected = CurrencyMismatchException.class)
    public void givenAmountWithDifferentCurrency_whenCredit_shouldThrowError() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        var amountToBeCredited = Amount.of(TEN, Currency.of("PHP"));

        account.credit(amountToBeCredited, AccountNumber.of("0000123456890"));
    }

    @Test(expected = DomainViolationException.class)
    public void givenSameAccountNumber_whenCredit_shouldThrowError() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        var amountToBeCredited = Amount.of(TEN, Currency.of("PHP"));

        account.credit(amountToBeCredited, AccountNumber.of("12345678901234"));
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
        assertThat(transaction.getAmount()).isEqualTo(amountToBeCredited);
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.CREDIT);
        assertThat(transaction.getAccountNumber()).isEqualTo(AccountNumber.of("0000123456890"));
        assertThat(transaction.getBalance()).isEqualTo(account.getBalance());
    }

    @Test
    public void givenActiveAccount_whenDeactivate_shouldMarkAccountAsInactive() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        account.deactivate();

        assertFalse(account.isActive());
    }

    @Test
    public void testAccept() {
        var account = Account.of(AccountNumber.of("12345678901234"), USD, TEN);
        account.credit(Amount.of(TEN, USD), AccountNumber.of("0000123456890"));

        var visitor = mock(AccountVisitor.class);
        account.accept(visitor);

        verify(visitor).setAccountNumber(eq(account.getNumber().getValue()));
        verify(visitor).setBalance(eq(account.getBalance().getAmount()));
        verify(visitor).setCurrency(eq(account.getCurrency().getValue()));

        ArgumentCaptor<List<TransactionTemplate>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(visitor).setTransactions(argumentCaptor.capture());

        var txnTemplates = argumentCaptor.getValue();
        assertThat(txnTemplates).hasSize(1);

        var template = txnTemplates.get(0);
        var transaction = account.getTransactions().get(0);

        assertThat(template.getAccountId()).isNull();
        assertThat(template.getAccountNumber()).isEqualTo(transaction.getAccountNumber().getValue());
        assertThat(template.getBalance()).isEqualTo(transaction.getBalance().getAmount());
        assertThat(template.getDate()).isEqualTo(transaction.getDate());
        assertThat(template.getType()).isEqualTo(transaction.getTransactionType().name());
    }
}