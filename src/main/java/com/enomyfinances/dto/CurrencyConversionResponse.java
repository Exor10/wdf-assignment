package com.enomyfinances.dto;

import java.math.BigDecimal;

public class CurrencyConversionResponse {
    private BigDecimal rate;
    private BigDecimal converted;
    private BigDecimal fee;
    private BigDecimal finalAmount;

    public CurrencyConversionResponse(BigDecimal rate, BigDecimal converted, BigDecimal fee, BigDecimal finalAmount) {
        this.rate = rate;
        this.converted = converted;
        this.fee = fee;
        this.finalAmount = finalAmount;
    }

    public BigDecimal getRate() { return rate; }
    public BigDecimal getConverted() { return converted; }
    public BigDecimal getFee() { return fee; }
    public BigDecimal getFinalAmount() { return finalAmount; }
}
