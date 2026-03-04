package com.enomyfinances.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "investment_projections")
public class InvestmentProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "investment_id", nullable = false)
    private Investment investment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DurationPeriod duration;

    @Column(name = "min_projection", nullable = false, precision = 19, scale = 2)
    private BigDecimal minProjection;

    @Column(name = "max_projection", nullable = false, precision = 19, scale = 2)
    private BigDecimal maxProjection;

    @Column(name = "min_profit", nullable = false, precision = 19, scale = 2)
    private BigDecimal minProfit;

    @Column(name = "max_profit", nullable = false, precision = 19, scale = 2)
    private BigDecimal maxProfit;

    @Column(name = "min_tax", nullable = false, precision = 19, scale = 2)
    private BigDecimal minTax;

    @Column(name = "max_tax", nullable = false, precision = 19, scale = 2)
    private BigDecimal maxTax;

    @Column(name = "total_fees_min", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalFeesMin;

    @Column(name = "total_fees_max", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalFeesMax;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Investment getInvestment() { return investment; }
    public void setInvestment(Investment investment) { this.investment = investment; }
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
