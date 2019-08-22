package com.github.tddiaz.moneytransferservice.app;

import com.github.tddiaz.moneytransferservice.domain.models.Account;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;
import com.github.tddiaz.moneytransferservice.domain.models.Currency;
import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.discovery.event.ServiceStartedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class DataBootstrap implements ApplicationEventListener<ServiceStartedEvent> {

    private final AccountRepository accountRepository;

    @Override
    public void onApplicationEvent(ServiceStartedEvent event) {
        accountRepository.save(List.of(
                Account.of(AccountNumber.of("12340987654321"), Currency.of("GBP"), BigDecimal.valueOf(5000)),
                Account.of(AccountNumber.of("09876543214321"), Currency.of("GBP"), BigDecimal.valueOf(5000)),
                Account.of(AccountNumber.of("78901234567890"), Currency.of("USD"), BigDecimal.valueOf(5000))
        ));
    }
}
