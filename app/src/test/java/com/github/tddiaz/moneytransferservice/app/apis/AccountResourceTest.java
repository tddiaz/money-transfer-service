package com.github.tddiaz.moneytransferservice.app.apis;

import com.github.tddiaz.moneytransferservice.app.TestData;
import com.github.tddiaz.moneytransferservice.app.data.AccountDto;
import com.github.tddiaz.moneytransferservice.app.data.TransferAmount;
import com.github.tddiaz.moneytransferservice.app.exceptions.AccountNotFoundException;
import com.github.tddiaz.moneytransferservice.app.services.AccountService;
import com.github.tddiaz.moneytransferservice.domain.models.TransactionTemplate;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.OK;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@MicronautTest
public class AccountResourceTest {

    private static final String BASE_URL = "/accounts/{accountNumber}";

    @Inject
    private AccountService accountService;

    @MockBean(AccountService.class)
    public AccountService accountService() {
        return mock(AccountService.class);
    }

    @Test
    void givenInvalidPayloadRequest_whenPostingTransferAmount_shouldReturnPayloadErrors() {
        given()
            .port(38000)
            .contentType(ContentType.JSON)
                .body("{}")
        .when()
            .post(BASE_URL + "/transfer-amount", "123123123123")
        .then()
            .statusCode(BAD_REQUEST.getCode())
                .body("payloadErrors", hasSize(5))
                .body("payloadErrors", containsInAnyOrder("Amount to debit is required", "Currency of amount to debit is required",
                        "Beneficiary account number is required", "Amount to credit is required", "Currency of amount to credit is required"));

        verifyZeroInteractions(accountService);
    }

    @Test
    void givenValidRequestPayload_whenPostingTransferAmount_shouldReturnSuccessResponse() {

        when(accountService.transferAmount(any(TransferAmount.class))).thenReturn(accountDto());

        var payload = TestData.validTransferAmountCommand();

        given()
            .port(38000)
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(BASE_URL + "/transfer-amount", payload.getPayeeAccountNumber())
        .then()
            .statusCode(OK.getCode())
            .body("accountNumber", is("12345678901234"))
            .body("balance", is(1000))
            .body("currency", is("GBP"))
            .body("transactions", hasSize(1))
            .body("transactions[0].amount", is(100))
            .body("transactions[0].payeeAccountNumber", is("89076543126788"))
            .body("transactions[0].transactionType", is("CREDIT"))
            .body("transactions[0].balance", is(1000))
            .body("transactions[0].date", notNullValue());

        verify(accountService).transferAmount(eq(payload));
    }

    @Test
    void givenInvalidAccountNumber_whenGetAccount_shouldReturnBadRequestWithMessage() {

        when(accountService.findByAccountNumber(anyString())).thenThrow(new AccountNotFoundException("account not found"));

        given()
            .port(38000)
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_URL, "123123123123")
        .then()
            .statusCode(BAD_REQUEST.getCode())
            .body("message", is("account not found"));

        verify(accountService).findByAccountNumber(eq("123123123123"));
    }

    @Test
    void givenValidAccountNumber_whenGetAccount_shouldReturnSucessResponse() {

        when(accountService.findByAccountNumber(anyString())).thenReturn(accountDto());

        given()
            .port(38000)
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_URL, "12345678901234")
        .then()
            .statusCode(OK.getCode())
            .body("accountNumber", is("12345678901234"))
            .body("balance", is(1000))
            .body("currency", is("GBP"))
            .body("transactions", hasSize(1))
            .body("transactions[0].amount", is(100))
            .body("transactions[0].payeeAccountNumber", is("89076543126788"))
            .body("transactions[0].transactionType", is("CREDIT"))
            .body("transactions[0].balance", is(1000))
            .body("transactions[0].date", notNullValue());

        verify(accountService).findByAccountNumber(eq("12345678901234"));
    }

    private static AccountDto accountDto() {
        var accountDto = new AccountDto();
        accountDto.setAccountNumber("12345678901234");
        accountDto.setBalance(BigDecimal.valueOf(1000L));
        accountDto.setCurrency("GBP");
        accountDto.setTransactions(Collections.singletonList(new TransactionTemplate() {
            @Override
            public String getAccountId() {
                return null;
            }

            @Override
            public BigDecimal getAmount() {
                return BigDecimal.valueOf(100L);
            }

            @Override
            public String getAccountNumber() {
                return "89076543126788";
            }

            @Override
            public String getType() {
                return "CREDIT";
            }

            @Override
            public LocalDateTime getDate() {
                return LocalDateTime.now();
            }

            @Override
            public BigDecimal getBalance() {
                return BigDecimal.valueOf(1000L);
            }
        }));

        return accountDto;
    }

}