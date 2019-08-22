package com.github.tddiaz.moneytransferservice.app.services;

import com.github.tddiaz.moneytransferservice.app.data.AccountDto;
import com.github.tddiaz.moneytransferservice.app.data.TransferAmount;
import com.github.tddiaz.moneytransferservice.app.exceptions.AccountNotFoundException;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;
import com.github.tddiaz.moneytransferservice.domain.models.Amount;
import com.github.tddiaz.moneytransferservice.domain.models.Currency;
import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import com.github.tddiaz.moneytransferservice.domain.services.TransferAmountService;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransferAmountService transferAmountService;

    public AccountDto transferAmount(TransferAmount command) {

        var payeeAccountNumber = AccountNumber.of(command.getPayeeAccountNumber());
        var beneficiaryAccountNumber = AccountNumber.of(command.getBeneficiaryAccountNumber());
        var amountToDebit = Amount.of(command.getAmountToDebitValue(), Currency.of(command.getAmountToDebitCurrency()));
        var amountToCredit = Amount.of(command.getAmountToCreditValue(), Currency.of(command.getAmountToCreditCurrency()));

        var account = transferAmountService.transferAmount(payeeAccountNumber, amountToDebit, beneficiaryAccountNumber, amountToCredit);
        var accountDto = new AccountDto();
        account.accept(accountDto);

        return accountDto;
    }

    public AccountDto findByAccountNumber(String accountNumber) {
        var account = accountRepository.findByAccountNumber(AccountNumber.of(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("account not found"));

        var accountDto = new AccountDto();
        account.accept(accountDto);

        return accountDto;
    }
}
