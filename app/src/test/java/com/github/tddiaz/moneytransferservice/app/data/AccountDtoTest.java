package com.github.tddiaz.moneytransferservice.app.data;

import com.github.tddiaz.moneytransferservice.app.TestData;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class AccountDtoTest {

    @Test
    public void testCreate() {
        var account = TestData.phpAccount();
        var accountDto = new AccountDto();
        account.accept(accountDto);

        Assertions.assertThat(accountDto.getAccountNumber()).isEqualTo(account.getNumber().getValue());
        Assertions.assertThat(accountDto.getCurrency()).isEqualTo(account.getCurrency().getValue());
        Assertions.assertThat(accountDto.getBalance()).isEqualTo(account.getBalance().getAmount());
        Assertions.assertThat(accountDto.getTransactions()).hasSize(0);
    }

}