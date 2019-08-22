package com.github.tddiaz.moneytransferservice.domain.services;

import com.github.tddiaz.moneytransferservice.domain.exceptions.AmountTransferException;
import com.github.tddiaz.moneytransferservice.domain.exceptions.DomainViolationException;
import com.github.tddiaz.moneytransferservice.domain.models.Account;
import com.github.tddiaz.moneytransferservice.domain.models.AccountNumber;
import com.github.tddiaz.moneytransferservice.domain.models.Amount;
import com.github.tddiaz.moneytransferservice.domain.repositories.AccountRepository;
import com.github.tddiaz.moneytransferservice.domain.utils.Validate;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class TransferAmountService {

    private final AccountRepository accountRepository;

    public Account transferAmount(AccountNumber payeeAccountNumber, Amount amountToDebitToPayee,
                                  AccountNumber beneficiaryAccountNumber, Amount AmountToCreditToBeneficiary) {
        Validate.requireNonNull(payeeAccountNumber, "payeeAccountNumber should not be null");
        Validate.requireNonNull(amountToDebitToPayee, "amountToDebitToPayee should not be null");
        Validate.requireNonNull(beneficiaryAccountNumber, "beneficiaryAccountNumber should not be null");
        Validate.requireNonNull(AmountToCreditToBeneficiary, "AmountToCreditToBeneficiary should not be null");

        if (Objects.equals(payeeAccountNumber, beneficiaryAccountNumber)) {
            throw new AmountTransferException("cannot perform transfer within same account");
        }

        var payeeAccount = accountRepository.findByAccountNumber(payeeAccountNumber)
                .orElseThrow(() -> new AmountTransferException("payee account not found"));

        var beneficiaryAccount = accountRepository.findByAccountNumber(beneficiaryAccountNumber)
                .orElseThrow(() -> new AmountTransferException("beneficiary account not found"));

        try {

            var lockedAccount1 = payeeAccount.hashCode() > beneficiaryAccount.hashCode() ? payeeAccount : beneficiaryAccount;
            var lockedAccount2 = payeeAccount.hashCode() > beneficiaryAccount.hashCode() ? beneficiaryAccount : payeeAccount;

            synchronized (lockedAccount1) {
                synchronized (lockedAccount2) {
                    payeeAccount.debitAmount(amountToDebitToPayee, beneficiaryAccountNumber);
                    beneficiaryAccount.creditAmount(AmountToCreditToBeneficiary, payeeAccountNumber);

                    accountRepository.save(List.of(payeeAccount, beneficiaryAccount));
                }
            }

        } catch (DomainViolationException e) {
            throw new AmountTransferException(e.getMessage(), e);
        }

        return payeeAccount;
    }
}
