package com.enomyfinances.entity;

public enum DurationPeriod {
    ONE_YEAR(12),
    FIVE_YEARS(60),
    TEN_YEARS(120);

    private final int months;

    DurationPeriod(int months) {
        this.months = months;
    }

    public int getMonths() {
        return months;
    }
}
