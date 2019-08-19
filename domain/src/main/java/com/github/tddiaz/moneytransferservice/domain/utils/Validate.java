package com.github.tddiaz.moneytransferservice.domain.utils;

import java.util.Optional;

public class Validate {
    public static void requireNonNull(Object o, String message) {
        Optional.ofNullable(o).orElseThrow(() -> new IllegalArgumentException(message));
    }
}
