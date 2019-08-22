package com.github.tddiaz.moneytransferservice.domain.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateTest {

    @Test
    void givenNullValue_whenValidate_shouldThrowError() {
        assertThrows(IllegalArgumentException.class, () -> Validate.requireNonNull(null, "error message"));
    }

}