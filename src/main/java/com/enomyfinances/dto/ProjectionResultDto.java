package com.enomyfinances.dto;

import com.enomyfinances.entity.DurationPeriod;
import java.math.BigDecimal;

public class ProjectionResultDto {
    private DurationPeriod duration;
    private BigDecimal minProjection;
    private BigDecimal maxProjection;
    private BigDecimal minProfit;
    private BigDecimal maxProfit;
    private BigDecimal minTax;
    private BigDecimal maxTax;
    private BigDecimal totalFeesMin;
    private BigDecimal totalFeesMax;

    public DurationPeriod getDuration() { return duration; }
    public void setDuration(DurationPeriod duration) { this.duration = duration; }
    public BigDecimal getMinProjection() { return minProjection; }
    public void setMinProjection(BigDecimal minProjection) { this.minProjection = minProjection; }
    public BigDecimal getMaxProjection() { return maxProjection; }
    public void setMaxProjection(BigDecimal maxProjection) { this.maxProjection = maxProjection; }
    public BigDecimal getMinProfit() { return minProfit; }
    public void setMinProfit(BigDecimal minProfit) { this.minProfit = minProfit; }
    public BigDecimal getMaxProfit() { return maxProfit; }
    public void setMaxProfit(BigDecimal maxProfit) { this.maxProfit = maxProfit; }
    public BigDecimal getMinTax() { return minTax; }
    public void setMinTax(BigDecimal minTax) { this.minTax = minTax; }
    public BigDecimal getMaxTax() { return maxTax; }
    public void setMaxTax(BigDecimal maxTax) { this.maxTax = maxTax; }
    public BigDecimal getTotalFeesMin() { return totalFeesMin; }
    public void setTotalFeesMin(BigDecimal totalFeesMin) { this.totalFeesMin = totalFeesMin; }
    public BigDecimal getTotalFeesMax() { return totalFeesMax; }
    public void setTotalFeesMax(BigDecimal totalFeesMax) { this.totalFeesMax = totalFeesMax; }
}
