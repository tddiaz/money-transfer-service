package com.github.tddiaz.moneytransferservice.app.data;

import com.github.tddiaz.moneytransferservice.domain.models.TransactionTemplate;
import com.github.tddiaz.moneytransferservice.domain.models.TransactionType;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionDtoTest {

    @Test
    public void givenCreditTransactionTemplate_whenCreate_shouldSetPayeeAccountNumber() {
        var template = creditTemplate();
        var transactionDto = TransactionDto.valueOf(template);

        assertThat(transactionDto.getAmount()).isEqualTo(template.getAmount());
        assertThat(transactionDto.getBalance()).isEqualTo(template.getBalance());
        assertThat(transactionDto.getTransactionType()).isEqualTo(template.getType());
        assertThat(transactionDto.getAmount()).isEqualTo(template.getAmount());
        assertThat(transactionDto.getPayeeAccountNumber()).isEqualTo(template.getAccountNumber());
        assertThat(transactionDto.getBeneficiaryAccountNumber()).isNull();
    }

    @Test
    public void givenDebitTransactionTemplate_whenCreate_shouldSetBeneficiaryAccountNumber() {
        var template = debitTemplate();
        var transactionDto = TransactionDto.valueOf(template);

        assertThat(transactionDto.getAmount()).isEqualTo(template.getAmount());
        assertThat(transactionDto.getBalance()).isEqualTo(template.getBalance());
        assertThat(transactionDto.getTransactionType()).isEqualTo(template.getType());
        assertThat(transactionDto.getAmount()).isEqualTo(template.getAmount());
        assertThat(transactionDto.getBeneficiaryAccountNumber()).isEqualTo(template.getAccountNumber());
        assertThat(transactionDto.getPayeeAccountNumber()).isNull();
    }

    private static TransactionTemplate creditTemplate() {
        return new TransactionTemplate() {
            @Override
            public String getAccountId() {
                return null;
            }

            @Override
            public BigDecimal getAmount() {
                return BigDecimal.TEN;
            }

            @Override
            public String getAccountNumber() {
                return "123123123123";
            }

            @Override
            public String getType() {
                return TransactionType.CREDIT.name();
            }

            @Override
            public LocalDateTime getDate() {
                return LocalDateTime.now();
            }

            @Override
            public BigDecimal getBalance() {
                return BigDecimal.TEN;
            }
        };
    }

    private static TransactionTemplate debitTemplate() {
        return new TransactionTemplate() {
            @Override
            public String getAccountId() {
                return null;
            }

            @Override
            public BigDecimal getAmount() {
                return BigDecimal.TEN;
            }

            @Override
            public String getAccountNumber() {
                return "123123123123";
            }

            @Override
            public String getType() {
                return TransactionType.DEBIT.name();
            }

            @Override
            public LocalDateTime getDate() {
                return LocalDateTime.now();
            }

            @Override
            public BigDecimal getBalance() {
                return BigDecimal.TEN;
            }
        };
    }

}