package com.enomyfinances.dto;

import com.enomyfinances.entity.CurrencyCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CurrencyConversionRequest {

    @NotNull
    private CurrencyCode sourceCurrency;

    @NotNull
    private CurrencyCode targetCurrency;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    public CurrencyCode getSourceCurrency() { return sourceCurrency; }
    public void setSourceCurrency(CurrencyCode sourceCurrency) { this.sourceCurrency = sourceCurrency; }
    public CurrencyCode getTargetCurrency() { return targetCurrency; }
    public void setTargetCurrency(CurrencyCode targetCurrency) { this.targetCurrency = targetCurrency; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
