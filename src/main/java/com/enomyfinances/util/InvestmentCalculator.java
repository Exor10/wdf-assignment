package com.enomyfinances.util;

import com.enomyfinances.dto.ProjectionResultDto;
import com.enomyfinances.entity.DurationPeriod;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class InvestmentCalculator {

    private InvestmentCalculator() {}

    public static ProjectionResultDto calculate(DurationPeriod period,
                                                BigDecimal principal,
                                                BigDecimal annualMinReturn,
                                                BigDecimal annualMaxReturn,
                                                BigDecimal monthlyFeeRate,
                                                TaxPolicy taxPolicy) {
        ProjectionResultDto dto = new ProjectionResultDto();
        dto.setDuration(period);

        BigDecimal minFinal = compound(principal, annualMinReturn, period.getMonths(), monthlyFeeRate);
        BigDecimal maxFinal = compound(principal, annualMaxReturn, period.getMonths(), monthlyFeeRate);

        BigDecimal minProfit = minFinal.subtract(principal);
        BigDecimal maxProfit = maxFinal.subtract(principal);

        BigDecimal minTax = taxPolicy.computeTax(minProfit);
        BigDecimal maxTax = taxPolicy.computeTax(maxProfit);

        BigDecimal totalFeesMin = minFinal.multiply(monthlyFeeRate).multiply(BigDecimal.valueOf(period.getMonths()));
        BigDecimal totalFeesMax = maxFinal.multiply(monthlyFeeRate).multiply(BigDecimal.valueOf(period.getMonths()));

        dto.setMinProjection(minFinal.setScale(2, RoundingMode.HALF_UP));
        dto.setMaxProjection(maxFinal.setScale(2, RoundingMode.HALF_UP));
        dto.setMinProfit(minProfit.setScale(2, RoundingMode.HALF_UP));
        dto.setMaxProfit(maxProfit.setScale(2, RoundingMode.HALF_UP));
        dto.setMinTax(minTax.setScale(2, RoundingMode.HALF_UP));
        dto.setMaxTax(maxTax.setScale(2, RoundingMode.HALF_UP));
        dto.setTotalFeesMin(totalFeesMin.setScale(2, RoundingMode.HALF_UP));
        dto.setTotalFeesMax(totalFeesMax.setScale(2, RoundingMode.HALF_UP));

        return dto;
    }

    private static BigDecimal compound(BigDecimal principal, BigDecimal annualRate, int months, BigDecimal monthlyFeeRate) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 12, RoundingMode.HALF_UP);
        BigDecimal amount = principal;
        for (int i = 0; i < months; i++) {
            amount = amount.multiply(BigDecimal.ONE.add(monthlyRate));
            amount = amount.multiply(BigDecimal.ONE.subtract(monthlyFeeRate));
        }
        return amount;
    }

    @FunctionalInterface
    public interface TaxPolicy {
        BigDecimal computeTax(BigDecimal profit);
    }
}
