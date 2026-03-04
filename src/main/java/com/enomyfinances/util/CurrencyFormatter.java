package com.enomyfinances.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyFormatter {

    private static final NumberFormat GBP_FORMAT = NumberFormat.getCurrencyInstance(Locale.UK);

    private CurrencyFormatter() {
    }

    public static String formatGbp(BigDecimal amount) {
        return GBP_FORMAT.format(amount);
    }
}
