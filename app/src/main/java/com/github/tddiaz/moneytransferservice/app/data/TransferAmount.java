package com.github.tddiaz.moneytransferservice.app.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Introspected
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferAmount {

    @JsonIgnore
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
}
