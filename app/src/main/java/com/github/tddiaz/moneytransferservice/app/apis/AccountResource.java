package com.github.tddiaz.moneytransferservice.app.apis;

import com.github.tddiaz.moneytransferservice.app.data.AccountDto;
import com.github.tddiaz.moneytransferservice.app.data.TransferAmount;
import com.github.tddiaz.moneytransferservice.app.services.AccountService;
import com.github.tddiaz.moneytransferservice.domain.exceptions.DomainViolationException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Validated
@Controller("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountResource {

    private final AccountService accountService;

    @Post("/{payeeAccountNumber}/transfer-amount")
    public HttpResponse<AccountDto> transerAmount(String payeeAccountNumber, @Valid @Body TransferAmount transferAmount) {
        LOGGER.info("received transfer amount request from account '{}'. payload '{}'", payeeAccountNumber, transferAmount);
        transferAmount.setPayeeAccountNumber(payeeAccountNumber);

        return HttpResponse.ok(accountService.transferAmount(transferAmount));
    }

    @Get("/{accountNumber}")
    public HttpResponse<AccountDto> getAccount(String accountNumber) {
        return HttpResponse.ok(accountService.findByAccountNumber(accountNumber));
    }

    @Error
    public HttpResponse handleError(HttpRequest request, ConstraintViolationException e) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("payloadErrors", e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList()));

        LOGGER.info("payload errors: {}", errors);

        return HttpResponse.badRequest(errors);
    }

    @Error
    public HttpResponse handleError(HttpRequest request, DomainViolationException e) {
        LOGGER.error("handling domain violation exception", e);
        return HttpResponse.badRequest("{\"message\": \"" + e.getMessage() + "\"}");
    }

    @Error
    public HttpResponse handleError(HttpRequest request, Exception e) {
        LOGGER.error("handling generic exception", e);
        return HttpResponse.serverError("{\"message\": \"service error\"}");
    }
}
