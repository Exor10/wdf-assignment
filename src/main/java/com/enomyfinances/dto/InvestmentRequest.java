package com.enomyfinances.dto;

import com.enomyfinances.entity.InvestmentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class InvestmentRequest {

    @NotNull
    private InvestmentType investmentType;

    @NotNull
    @DecimalMin("100.00")
    private BigDecimal principal;

    public InvestmentType getInvestmentType() { return investmentType; }
    public void setInvestmentType(InvestmentType investmentType) { this.investmentType = investmentType; }
    public BigDecimal getPrincipal() { return principal; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }
}
