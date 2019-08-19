package com.github.tddiaz.moneytransferservice.domain.utils;

import org.junit.Test;

public class ValidateTest {

    @Test(expected = IllegalArgumentException.class)
    public void givenNullValue_whenValidate_shouldThrowError() {
        Validate.requireNonNull(null, "error message");
    }

}