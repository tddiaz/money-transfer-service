package com.github.tddiaz.moneytransferservice.app;

import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import io.micronaut.runtime.Micronaut;

import javax.inject.Inject;

public class Application {

    @Inject
    private AccountRepository accountRepository;

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }


}