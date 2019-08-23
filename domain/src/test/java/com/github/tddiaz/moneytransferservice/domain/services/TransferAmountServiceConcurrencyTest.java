package com.github.tddiaz.moneytransferservice.domain.services;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.github.tddiaz.moneytransferservice.domain.models.Account;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;
import com.github.tddiaz.moneytransferservice.domain.models.Amount;
import com.github.tddiaz.moneytransferservice.domain.models.Currency;
import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ConcurrentTestRunner.class)
@Slf4j
public class TransferAmountServiceConcurrencyTest {

    private TransferAmountService transferAmountService;
    private AccountRepository accountRepository;

    public TransferAmountServiceConcurrencyTest() {
        this.accountRepository = new AccountRepositoryImpl();
        this.transferAmountService = new TransferAmountService(accountRepository);
    }

    @Test
    @ThreadCount(100)
    public void testDeadlock() {

        {
            var payee = AccountNumber.of("12340987654321");
            var amountToDebit = Amount.of(BigDecimal.valueOf(100L), Currency.of("GBP"));
            var beneficiary = AccountNumber.of("09876543214321");
            var amountToCredit = Amount.of(BigDecimal.valueOf(100L), Currency.of("GBP"));

            transferAmountService.transferAmount(payee, amountToDebit, beneficiary, amountToCredit);
        }


        {
            var payee = AccountNumber.of("09876543214321");
            var amountToDebit = Amount.of(BigDecimal.valueOf(100L), Currency.of("GBP"));
            var beneficiary = AccountNumber.of("12340987654321");
            var amountToCredit = Amount.of(BigDecimal.valueOf(100L), Currency.of("GBP"));

            transferAmountService.transferAmount(payee, amountToDebit, beneficiary, amountToCredit);
        }

    }

    @After
    public void testCount() {
        var payeeAccount = accountRepository.findByAccountNumber(AccountNumber.of("12340987654321")).get();
        assertThat(payeeAccount.getBalance().getAmount()).isEqualTo(BigDecimal.valueOf(5000));

        var beneficiaryAccount = accountRepository.findByAccountNumber(AccountNumber.of("09876543214321")).get();
        assertThat(beneficiaryAccount.getBalance().getAmount()).isEqualTo(BigDecimal.valueOf(5000));
    }

    private class AccountRepositoryImpl implements AccountRepository {

        private final Map<String, Account> ACCOUNTS = new ConcurrentHashMap<>();

        private AccountRepositoryImpl() {
            ACCOUNTS.put("12340987654321", Account.of(AccountNumber.of("12340987654321"), Currency.of("GBP"), BigDecimal.valueOf(5000)));
            ACCOUNTS.put("09876543214321", Account.of(AccountNumber.of("09876543214321"), Currency.of("GBP"), BigDecimal.valueOf(5000)));
        }

        @Override
        public Optional<Account> findByAccountNumber(AccountNumber accountNumber) {
            return Optional.ofNullable(ACCOUNTS.get(accountNumber.getValue()));
        }

        @Override
        public Account save(Account account) {
            return null;
        }

        @Override
        public Collection<Account> save(Collection<Account> accounts) {
            return null;
        }
    }
}
