package com.github.tddiaz.moneytransferservice.domain.models;

import java.math.BigDecimal;
import java.util.List;

public interface AccountVisitor {
    void setAccountNumber(String accountNumber);
    void setCurrency(String currency);
    void setBalance(BigDecimal balance);
    void setTransactions(List<TransactionTemplate> transactions);
}
