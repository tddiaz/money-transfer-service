package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.CurrencyMismatchException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.DomainViolationException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.InactiveAccountException;
import com.github.tddiaz.moneytransferservice.domain.utils.Validate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        verifyAccountNumberElseThrow(beneficiaryAccountNumber);
        verifyCurrencyOfAmountElseThrow(amountToDebit);

        if (!active) {
            throw new InactiveAccountException("payee account is inactive");
        }

        this.balance = balance.deduct(amountToDebit);

        addTransaction(Transaction.asDebit(id, beneficiaryAccountNumber, balance));
    }

    public void credit(Amount amountToCredit, AccountNumber payeeAccountNumber) {
        Validate.requireNonNull(amountToCredit, "amountToCredit should not be null");
        Validate.requireNonNull(payeeAccountNumber, "payeeAccountNumber should not be null");

        verifyAccountNumberElseThrow(payeeAccountNumber);
        verifyCurrencyOfAmountElseThrow(amountToCredit);

        if (!active) {
            throw new InactiveAccountException("beneficiary account is inactive");
        }

        this.balance = balance.add(amountToCredit);

        addTransaction(Transaction.asCredit(id, payeeAccountNumber, balance));
    }

    public void deactivate() {
        this.active = false;
    }

    private void verifyCurrencyOfAmountElseThrow(Amount amount) {
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

    private void verifyAccountNumberElseThrow(AccountNumber accountNumber) {
        if (Objects.equals(number, accountNumber)) {
            throw new DomainViolationException("cannot perform debit/credit of amount within same account");
        }
    }

    public void accept(AccountVisitor visitor) {
        visitor.setAccountNumber(number.getValue());
        visitor.setCurrency(currency.getValue());
        visitor.setBalance(balance.getAmount());
        visitor.setTransactions(CollectionUtils.isEmpty(transactions) ? Collections.emptyList() :
                transactions.stream().map(txn -> new TransactionTemplate() {
                    @Override
                    public String getAccountId() {
                        return null;
                    }

                    @Override
                    public String getAccountNumber() {
                        return txn.getAccountNumber().getValue();
                    }

                    @Override
                    public String getType() {
                        return txn.getType().name();
                    }

                    @Override
                    public LocalDateTime getDate() {
                        return txn.getDate();
                    }

                    @Override
                    public BigDecimal getBalance() {
                        return txn.getBalance().getAmount();
                    }
                }).collect(Collectors.toList()));
    }
}
