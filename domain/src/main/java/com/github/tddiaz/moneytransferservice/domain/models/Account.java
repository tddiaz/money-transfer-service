package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.CurrencyMismatchException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.InactiveAccountException;
import com.github.tddiaz.moneytransferservice.domain.utils.Validate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Setter(AccessLevel.NONE)
public class Account {

    private AccountId id;
    private AccountNumber number;
    private Currency currency;
    private Balance balance;
    private boolean active;
    private List<Transaction> transactions;

    private Account(AccountNumber number, Currency currency, Balance balance) {
        this.id = AccountId.create();
        this.number = number;
        this.currency = currency;
        this.balance = balance;
        this.active = true;
    }

    public static Account of(AccountNumber number, Currency currency, BigDecimal balanceValue) {
        Validate.requireNonNull(number, "number should not be null");
        Validate.requireNonNull(currency, "currency should not be null");
        Validate.requireNonNull(balanceValue, "balanceValue should not be null");

        return new Account(number, currency, Balance.of(Amount.of(balanceValue, currency)));
    }

    public void debit(Amount amountToDebit, AccountNumber beneficiaryAccountNumber) {
        Validate.requireNonNull(amountToDebit, "amountToDebit should not be null");
        Validate.requireNonNull(beneficiaryAccountNumber, "beneficiaryAccountNumber should not be null");

        verifyCurrencyOfAmount(amountToDebit);

        if (!active) {
            throw new InactiveAccountException("payee account is inactive");
        }

        this.balance = balance.deduct(amountToDebit);

        addTransaction(Transaction.asDebit(id, beneficiaryAccountNumber, balance));
    }

    public void credit(Amount amountToCredit, AccountNumber payeeAccountNumber) {
        Validate.requireNonNull(amountToCredit, "amountToCredit should not be null");
        Validate.requireNonNull(payeeAccountNumber, "payeeAccountNumber should not be null");

        verifyCurrencyOfAmount(amountToCredit);

        if (!active) {
            throw new InactiveAccountException("beneficiary account is inactive");
        }

        this.balance = balance.add(amountToCredit);

        addTransaction(Transaction.asCredit(id, payeeAccountNumber, balance));
    }

    public void deactivate() {
        this.active = false;
    }

    private void verifyCurrencyOfAmount(Amount amount) {
        if (!Objects.equals(this.currency.getValue(), amount.getCurrency())) {
            throw new CurrencyMismatchException("account cannot accept different currency");
        }
    }

    private void addTransaction(Transaction transaction) {
        if (CollectionUtils.isEmpty(transactions)) {
            this.transactions = new ArrayList<>();
        }

        transactions.add(transaction);
    }
}
