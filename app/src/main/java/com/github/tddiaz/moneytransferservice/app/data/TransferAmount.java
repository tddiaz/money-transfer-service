package com.github.tddiaz.moneytransferservice.app.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Introspected
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferAmount {

    @NotBlank(message = "Payee account number is required")
    private String payeeAccountNumber;

    @NotNull(message = "Amount to debit is required")
    private BigDecimal amountToDebitValue;

    @NotBlank(message = "Currency of amount to debit is required")
    private String amountToDebitCurrency;

    @NotBlank(message = "Beneficiary account number is required")
    private String beneficiaryAccountNumber;

    @NotNull(message = "Amount to credit is required")
    private BigDecimal amountToCreditValue;

    @NotBlank(message = "Currency of amount to credit is required")
    private String amountToCreditCurrency;

    @Override
    public String toString() {
        return "TransferAmount{" +
                "payeeAccountNumber='" + payeeAccountNumber + '\'' +
                ", amountToDebitValue=" + amountToDebitValue +
                ", amountToDebitCurrency='" + amountToDebitCurrency + '\'' +
                ", beneficiaryAccountNumber='" + beneficiaryAccountNumber + '\'' +
                ", amountToCreditValue=" + amountToCreditValue +
                ", amountToCreditCurrency='" + amountToCreditCurrency + '\'' +
                '}';
    }
}
