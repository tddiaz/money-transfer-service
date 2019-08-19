package com.github.tddiaz.moneytransferservice.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionTemplate {
    String getAccountId();
    String getAccountNumber();
    String getType();
    LocalDateTime getDate();
    BigDecimal getBalance();
}
