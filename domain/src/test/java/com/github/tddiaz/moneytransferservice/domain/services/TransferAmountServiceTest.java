package com.github.tddiaz.moneytransferservice.domain.services;

import com.github.tddiaz.moneytransferservice.domain.exceptions.AmountTransferException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.InsufficientFundsException;
import com.github.tddiaz.moneytransferservice.domain.models.Account;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;
import com.github.tddiaz.moneytransferservice.domain.models.Amount;
import com.github.tddiaz.moneytransferservice.domain.models.Currency;
import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferAmountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferAmountService transferAmountService;

    private static final Amount AMOUNT_TO_DEBIT = Amount.of(BigDecimal.TEN, Currency.of("USD"));
    private static final Amount AMOUNT_TO_CREDIT = Amount.of(BigDecimal.TEN, Currency.of("USD"));
    private static final AccountNumber PAYEE_ACCOUNT_NUMBER = AccountNumber.of("12345678901010");
    private static final AccountNumber BENEFICIARY_ACCOUNT_NUMBER = AccountNumber.of("09876543211234");

    @Test
    public void givenSameAccountNumber_whenTransferAmount_shouldThrowError() {
        assertThrows(AmountTransferException.class, () ->
                transferAmountService.transferAmount(PAYEE_ACCOUNT_NUMBER, AMOUNT_TO_DEBIT, PAYEE_ACCOUNT_NUMBER, AMOUNT_TO_CREDIT));
    }

    @Test
    public void givenInvalidPayeeAccountNumber_whenTransferAmount_shouldThrowError() {
        when(accountRepository.findByAccountNumber(eq(PAYEE_ACCOUNT_NUMBER)))
                .thenReturn(Optional.empty());

        assertThrows(AmountTransferException.class, () ->
                transferAmountService.transferAmount(PAYEE_ACCOUNT_NUMBER, AMOUNT_TO_DEBIT, BENEFICIARY_ACCOUNT_NUMBER, AMOUNT_TO_CREDIT));
    }

    @Test
    public void givenInvalidBeneficiaryAccountNumber_whenTransferAmount_shouldThrowError() {
        when(accountRepository.findByAccountNumber(eq(PAYEE_ACCOUNT_NUMBER)))
                .thenReturn(Optional.of(mock(Account.class)));

        when(accountRepository.findByAccountNumber(eq(BENEFICIARY_ACCOUNT_NUMBER)))
                .thenReturn(Optional.empty());

        assertThrows(AmountTransferException.class, () ->
                transferAmountService.transferAmount(PAYEE_ACCOUNT_NUMBER, AMOUNT_TO_DEBIT, BENEFICIARY_ACCOUNT_NUMBER, AMOUNT_TO_CREDIT));
    }

    @Test
    public void whenTransferAmountAndModelErrorOccurred_shouldThrowAmountTransferError() {
        var payeeAccount = mock(Account.class);
        doThrow(InsufficientFundsException.class).when(payeeAccount).debitAmount(any(), any());
        when(accountRepository.findByAccountNumber(PAYEE_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(payeeAccount));

        var beneficiaryAccount = mock(Account.class);
        when(accountRepository.findByAccountNumber(BENEFICIARY_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(beneficiaryAccount));

        assertThrows(AmountTransferException.class, () ->
                transferAmountService.transferAmount(PAYEE_ACCOUNT_NUMBER, AMOUNT_TO_DEBIT, BENEFICIARY_ACCOUNT_NUMBER, AMOUNT_TO_CREDIT));
    }

    @Test
    public void givenValidParams_whenTransferAmount_shouldReturnPayeeAccount() {
        var payeeAccount = mock(Account.class);
        when(accountRepository.findByAccountNumber(PAYEE_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(payeeAccount));

        var beneficiaryAccount = mock(Account.class);
        when(accountRepository.findByAccountNumber(BENEFICIARY_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(beneficiaryAccount));

        var account = transferAmountService.transferAmount(PAYEE_ACCOUNT_NUMBER, AMOUNT_TO_DEBIT, BENEFICIARY_ACCOUNT_NUMBER, AMOUNT_TO_CREDIT);

        Assertions.assertThat(account).isEqualTo(payeeAccount);
        verify(payeeAccount).debitAmount(AMOUNT_TO_DEBIT, BENEFICIARY_ACCOUNT_NUMBER);
        verify(beneficiaryAccount).creditAmount(AMOUNT_TO_DEBIT, PAYEE_ACCOUNT_NUMBER);
        verify(accountRepository).save(anyList());
    }
}