package com.github.tddiaz.moneytransferservice.app;

import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import com.github.tddiaz.moneytransferservice.domain.services.TransferAmountService;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class BeanFactory {

    @Bean
    @Singleton
    public TransferAmountService transferAmountService(AccountRepository accountRepository) {
        return new TransferAmountService(accountRepository);
    }
}
