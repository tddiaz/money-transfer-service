package com.github.tddiaz.moneytransferservice.app.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.tddiaz.moneytransferservice.domain.models.TransactionTemplate;
import com.github.tddiaz.moneytransferservice.domain.models.TransactionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

    private BigDecimal amount;
    private String payeeAccountNumber;
    private String beneficiaryAccountNumber;
    private String transactionType;
    private BigDecimal balance;
    private String date;

    public static TransactionDto valueOf(TransactionTemplate template) {
        var transactionDto = new TransactionDto();
        transactionDto.amount = template.getAmount();
        transactionDto.balance = template.getBalance();
        transactionDto.transactionType = template.getType();
        transactionDto.date = DateTimeFormatter.ISO_DATE.format(template.getDate());

        if (Objects.equals(template.getType(), TransactionType.CREDIT.name())) {
            transactionDto.payeeAccountNumber = template.getAccountNumber();
        } else {
            transactionDto.beneficiaryAccountNumber = template.getAccountNumber();
        }

        return transactionDto;
    }
}
