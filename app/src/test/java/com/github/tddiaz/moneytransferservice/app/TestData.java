package com.github.tddiaz.moneytransferservice.app;

import com.github.tddiaz.moneytransferservice.app.data.TransferAmount;
import com.github.tddiaz.moneytransferservice.domain.models.Account;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;
import com.github.tddiaz.moneytransferservice.domain.models.Currency;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;

public class TestData {

    private static final Currency USD = Currency.of("USD");
    private static final Currency PHP = Currency.of("PHP");

    public static Account usdAccount() {
        return Account.of(AccountNumber.of("12345678901234"), USD, TEN);
    }

    public static Account phpAccount() {
        return Account.of(AccountNumber.of("00001234567890"), PHP, TEN);
    }

    public static TransferAmount validTransferAmountCommand() {
        var transferAmount = new TransferAmount();
        transferAmount.setPayeeAccountNumber("12345678901234");
        transferAmount.setAmountToDebitCurrency("USD");
        transferAmount.setAmountToDebitValue(BigDecimal.valueOf(2));
        transferAmount.setBeneficiaryAccountNumber("12345678900000");
        transferAmount.setAmountToCreditCurrency("USD");
        transferAmount.setAmountToCreditValue(BigDecimal.valueOf(2));

        return transferAmount;
    }
}
