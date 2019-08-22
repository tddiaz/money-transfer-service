package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.exceptions.CurrencyMismatchException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.DomainViolationException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.InactiveAccountException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
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

    public static Account of(@NonNull AccountNumber number, @NonNull Currency currency, @NonNull BigDecimal balanceValue) {
        return new Account(number, currency, Balance.of(Amount.of(balanceValue, currency)));
    }

    public void debitAmount(@NonNull Amount amountToDebit, @NonNull  AccountNumber beneficiaryAccountNumber) {
        verifyAccountNumberElseThrow(beneficiaryAccountNumber);
        verifyCurrencyOfAmountElseThrow(amountToDebit);

        if (!active) {
            throw new InactiveAccountException("payee account is inactive");
        }

        this.balance = balance.deduct(amountToDebit);

        addTransaction(Transaction.asDebit(id, amountToDebit, beneficiaryAccountNumber, balance));
    }

    public void creditAmount(@NonNull Amount amountToCredit, @NonNull AccountNumber payeeAccountNumber) {
        verifyAccountNumberElseThrow(payeeAccountNumber);
        verifyCurrencyOfAmountElseThrow(amountToCredit);

        if (!active) {
            throw new InactiveAccountException("beneficiary account is inactive");
        }

        this.balance = balance.add(amountToCredit);

        addTransaction(Transaction.asCredit(id, amountToCredit, payeeAccountNumber, balance));
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
                    public BigDecimal getAmount() {
                        return txn.getAmount().getValue();
                    }

                    @Override
                    public String getAccountNumber() {
                        return txn.getAccountNumber().getValue();
                    }

                    @Override
                    public String getType() {
                        return txn.getTransactionType().name();
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
