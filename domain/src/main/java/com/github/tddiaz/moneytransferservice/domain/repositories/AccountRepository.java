package com.github.tddiaz.moneytransferservice.domain.repositories;

import com.github.tddiaz.moneytransferservice.domain.models.Account;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByAccountNumber(AccountNumber accountNumber);
    Account save(Account account);
    Collection<Account> save(Collection<Account> accounts);
}
