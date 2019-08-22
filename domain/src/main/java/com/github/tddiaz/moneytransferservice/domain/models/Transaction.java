package com.github.tddiaz.moneytransferservice.domain.models;

import lombok.NonNull;
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

    public static Transaction asDebit(@NonNull AccountId accountId, @NonNull Amount amount,
                                      @NonNull AccountNumber beneficiaryAccountNumber, @NonNull Balance balance) {
        return new Transaction(accountId, amount, TransactionType.DEBIT, beneficiaryAccountNumber, balance);
    }

    public static Transaction asCredit(@NonNull AccountId accountId, @NonNull Amount amount,
                                       @NonNull AccountNumber payeeAccountNumber, @NonNull Balance balance) {
        return new Transaction(accountId, amount, TransactionType.CREDIT, payeeAccountNumber, balance);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "accountId=" + accountId +
                ", amount=" + amount +
                ", transactionType=" + transactionType +
                ", accountNumber=" + accountNumber +
                ", balance=" + balance +
                ", date=" + date +
                '}';
    }
}
