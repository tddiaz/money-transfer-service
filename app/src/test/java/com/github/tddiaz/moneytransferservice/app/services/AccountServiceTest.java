package com.github.tddiaz.moneytransferservice.app.services;

import com.github.tddiaz.moneytransferservice.app.TestData;
import com.github.tddiaz.moneytransferservice.app.exceptions.AccountNotFoundException;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;
import com.github.tddiaz.moneytransferservice.domain.models.Amount;
import com.github.tddiaz.moneytransferservice.domain.models.Currency;
import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import com.github.tddiaz.moneytransferservice.domain.services.TransferAmountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferAmountService transferAmountService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void givenValidParams_whenTransferAmount_shouldReturnAccountDto() {
        when(transferAmountService.transferAmount(any(), any(), any(), any())).thenReturn(TestData.usdAccount());

        var transferAmount = TestData.validTransferAmountCommand();
        var accountDto = accountService.transferAmount(transferAmount);

        var debitAmount = Amount.of(transferAmount.getAmountToDebitValue(), Currency.of(transferAmount.getAmountToDebitCurrency()));
        var beneficiaryAccountNumber = AccountNumber.of(transferAmount.getBeneficiaryAccountNumber());
        var amountToCredit = Amount.of(transferAmount.getAmountToCreditValue(), Currency.of(transferAmount.getAmountToCreditCurrency()));
        var payeeAccountNumber = AccountNumber.of(transferAmount.getPayeeAccountNumber());

        assertNotNull(accountDto);
        verify(transferAmountService).transferAmount(eq(payeeAccountNumber), eq(debitAmount), eq(beneficiaryAccountNumber), eq(amountToCredit));
    }

    @Test
    void givenInvalidAccountNumber_whenFindByAccountNumber_shouldThrowError() {
        when(accountRepository.findByAccountNumber(any(AccountNumber.class))).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.findByAccountNumber("12341234567890");
        });
    }

    @Test
    void givenValidAccountNumber_whenFindByAccountNumber_shouldReturnAccountDto() {
        when(accountRepository.findByAccountNumber(any(AccountNumber.class))).thenReturn(Optional.of(TestData.phpAccount()));
        var accountDto = accountService.findByAccountNumber("12341234567890");
        assertNotNull(accountDto);
        verify(accountRepository).findByAccountNumber(eq(AccountNumber.of("12341234567890")));
    }

}