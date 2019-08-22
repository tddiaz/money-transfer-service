package com.github.tddiaz.moneytransferservice.app.repositories;

import com.github.tddiaz.moneytransferservice.domain.models.Account;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;
import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Slf4j
class HashMapAccountRepository implements AccountRepository {

    private static final Map<String, Account> ACCOUNTS = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findByAccountNumber(AccountNumber accountNumber) {
        return Optional.ofNullable(ACCOUNTS.get(accountNumber.getValue()));
    }

    @Override
    public Account save(Account account) {
        LOGGER.info("inserting account: {}", account);
        ACCOUNTS.put(account.getNumber().getValue(), account);
        return account;
    }

    @Override
    public Collection<Account> save(Collection<Account> accounts) {
        accounts.forEach(this::save);
        return accounts;
    }
}
