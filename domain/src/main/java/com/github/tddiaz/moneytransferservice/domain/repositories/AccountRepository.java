package com.github.tddiaz.moneytransferservice.domain.repositories;

import com.github.tddiaz.moneytransferservice.domain.models.Account;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByAccountNumber(AccountNumber accountNumber);
    Account update(Account account);
}
