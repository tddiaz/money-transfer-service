package com.github.tddiaz.moneytransferservice.domain.models;

import com.github.tddiaz.moneytransferservice.domain.utils.Validate;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Transaction {

    private AccountId accountId;
    private Transaction.Type type;
    private AccountNumber accountNumber;
    private Balance balance;
    private LocalDateTime date;

    private Transaction(AccountId accountId, Type type, AccountNumber accountNumber, Balance balance) {
        this.accountId = accountId;
        this.type = type;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.date = LocalDateTime.now();
    }

    public static Transaction asDebit(AccountId accountId, AccountNumber beneficiaryAccountNumber, Balance balance) {
        Validate.requireNonNull(accountId, "accountId should not be null");
        Validate.requireNonNull(beneficiaryAccountNumber, "beneficiaryAccountNumber should not be null");
        Validate.requireNonNull(balance, "balance should not be null");

        return new Transaction(accountId, Type.DEBIT, beneficiaryAccountNumber, balance);
    }

    public static Transaction asCredit(AccountId accountId, AccountNumber payeeAccountNumber, Balance balance) {
        Validate.requireNonNull(accountId, "accountId should not be null");
        Validate.requireNonNull(payeeAccountNumber, "payeeAccountNumber should not be null");
        Validate.requireNonNull(balance, "balance should not be null");

        return new Transaction(accountId, Type.CREDIT, payeeAccountNumber, balance);
    }

    public enum Type {
        DEBIT, CREDIT
    }
}
