package com.github.tddiaz.moneytransferservice.app.data;

import com.github.tddiaz.moneytransferservice.domain.models.AccountVisitor;
import com.github.tddiaz.moneytransferservice.domain.models.TransactionTemplate;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AccountDto implements AccountVisitor {

    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private List<TransactionDto> transactions;

    @Override
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public void setTransactions(List<TransactionTemplate> transactions) {
        this.transactions = transactions.stream().sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                .map(TransactionDto::valueOf).collect(Collectors.toList());
    }
}
