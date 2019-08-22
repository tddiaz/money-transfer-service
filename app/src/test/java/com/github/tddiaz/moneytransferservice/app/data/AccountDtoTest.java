package com.github.tddiaz.moneytransferservice.app.data;

import com.github.tddiaz.moneytransferservice.app.TestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class AccountDtoTest {

    @Test
    void testCreate() {
        var account = TestData.phpAccount();
        var accountDto = new AccountDto();
        account.accept(accountDto);

        assertThat(accountDto.getAccountNumber()).isEqualTo(account.getNumber().getValue());
        assertThat(accountDto.getCurrency()).isEqualTo(account.getCurrency().getValue());
        assertThat(accountDto.getBalance()).isEqualTo(account.getBalance().getAmount());
        assertThat(accountDto.getTransactions()).hasSize(0);
    }

}