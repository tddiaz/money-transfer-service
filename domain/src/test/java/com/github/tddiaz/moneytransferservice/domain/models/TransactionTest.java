package com.github.tddiaz.moneytransferservice.domain.models;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionTest {

    private static final AccountId accountId = AccountId.create();
    private static final AccountNumber accountNumber = AccountNumber.of("12345678901234");
    private static final Balance balance = Balance.of(Amount.of(BigDecimal.TEN, Currency.of("USD")));

    @Test
    public void givenValidParams_whenCreateAsDebit_shouldCreateTransaction() {
        var transaction = Transaction.asDebit(accountId, accountNumber, balance);
        assertThat(transaction.getType()).isEqualTo(Transaction.Type.DEBIT);
        assertThat(transaction.getAccountId()).isEqualTo(accountId);
        assertThat(transaction.getBalance()).isEqualTo(balance);
        assertThat(transaction.getAccountNumber()).isEqualTo(accountNumber);
    }

    @Test
    public void givenValidParams_whenCreateAsCredit_shouldCreateTransaction() {
        var transaction = Transaction.asCredit(accountId, accountNumber, balance);
        assertThat(transaction.getType()).isEqualTo(Transaction.Type.CREDIT);
        assertThat(transaction.getAccountId()).isEqualTo(accountId);
        assertThat(transaction.getBalance()).isEqualTo(balance);
        assertThat(transaction.getAccountNumber()).isEqualTo(accountNumber);
    }

}