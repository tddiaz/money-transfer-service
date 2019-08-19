package com.github.tddiaz.moneytransferservice.domain.models;

import java.math.BigDecimal;
import java.util.List;

public interface AccountTemplate {
    String getId();
    String getNumber();
    boolean isActive();
    String getCurrency();
    BigDecimal getBalance();
    List<TransactionTemplate> getTransactions();
}
