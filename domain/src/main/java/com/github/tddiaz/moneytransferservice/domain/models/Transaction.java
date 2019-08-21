package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.utils.Validate;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Transaction {

    private AccountId accountId;
    private Amount amount;
    private TransactionType transactionType;
    private AccountNumber accountNumber;
    private Balance balance;
    private LocalDateTime date;

    private Transaction(AccountId accountId, Amount amount, TransactionType transactionType, AccountNumber accountNumber, Balance balance) {
        this.accountId = accountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.date = LocalDateTime.now();
    }

    public static Transaction asDebit(AccountId accountId, Amount amount, AccountNumber beneficiaryAccountNumber, Balance balance) {
        Validate.requireNonNull(accountId, "accountId should not be null");
        Validate.requireNonNull(amount, "amount should not be null");
        Validate.requireNonNull(beneficiaryAccountNumber, "beneficiaryAccountNumber should not be null");
        Validate.requireNonNull(balance, "balance should not be null");

        return new Transaction(accountId, amount, TransactionType.DEBIT, beneficiaryAccountNumber, balance);
    }

    public static Transaction asCredit(AccountId accountId, Amount amount, AccountNumber payeeAccountNumber, Balance balance) {
        Validate.requireNonNull(accountId, "accountId should not be null");
        Validate.requireNonNull(amount, "amount should not be null");
        Validate.requireNonNull(payeeAccountNumber, "payeeAccountNumber should not be null");
        Validate.requireNonNull(balance, "balance should not be null");

        return new Transaction(accountId, amount, TransactionType.CREDIT, payeeAccountNumber, balance);
    }
}
