package com.enomyfinances.service;

import com.enomyfinances.dto.InvestmentRequest;
import com.enomyfinances.dto.ProjectionResultDto;
import com.enomyfinances.entity.DurationPeriod;
import com.enomyfinances.entity.Investment;
import com.enomyfinances.entity.InvestmentProjection;
import com.enomyfinances.entity.InvestmentType;
import com.enomyfinances.entity.User;
import com.enomyfinances.exception.InvestmentCalculationException;
import com.enomyfinances.exception.ValidationException;
import com.enomyfinances.repository.InvestmentProjectionRepository;
import com.enomyfinances.repository.InvestmentRepository;
import com.enomyfinances.repository.UserRepository;
import com.enomyfinances.util.InvestmentCalculator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final InvestmentProjectionRepository projectionRepository;
    private final UserRepository userRepository;

    public InvestmentService(InvestmentRepository investmentRepository,
                             InvestmentProjectionRepository projectionRepository,
                             UserRepository userRepository) {
        this.investmentRepository = investmentRepository;
        this.projectionRepository = projectionRepository;
        this.userRepository = userRepository;
    }

    public List<ProjectionResultDto> calculateAndSave(InvestmentRequest request, Authentication authentication) {
        if (request.getPrincipal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Investment principal must be positive");
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ValidationException("Authenticated user not found"));

        Investment investment = new Investment();
        investment.setUser(user);
        investment.setInvestmentType(request.getInvestmentType());
        investment.setPrincipalAmount(request.getPrincipal());
        investment.setCreatedAt(LocalDateTime.now());
        Investment savedInvestment = investmentRepository.save(investment);

        InvestmentMetadata metadata = metadataByType().get(request.getInvestmentType());
        if (metadata == null) {
            throw new InvestmentCalculationException("Unsupported investment type");
        }

        List<ProjectionResultDto> results = new ArrayList<>();
        for (DurationPeriod period : DurationPeriod.values()) {
            ProjectionResultDto result = InvestmentCalculator.calculate(
                    period,
                    request.getPrincipal(),
                    metadata.minAnnualReturn,
                    metadata.maxAnnualReturn,
                    metadata.monthlyFeeRate,
                    profit -> calculateTax(profit, request.getInvestmentType())
            );
            results.add(result);
            projectionRepository.save(mapProjection(savedInvestment, result));
        }

        return results;
    }

    private InvestmentProjection mapProjection(Investment investment, ProjectionResultDto dto) {
        InvestmentProjection projection = new InvestmentProjection();
        projection.setInvestment(investment);
        projection.setDuration(dto.getDuration());
        projection.setMinProjection(dto.getMinProjection());
        projection.setMaxProjection(dto.getMaxProjection());
        projection.setMinProfit(dto.getMinProfit());
        projection.setMaxProfit(dto.getMaxProfit());
        projection.setMinTax(dto.getMinTax());
        projection.setMaxTax(dto.getMaxTax());
        projection.setTotalFeesMin(dto.getTotalFeesMin());
        projection.setTotalFeesMax(dto.getTotalFeesMax());
        return projection;
    }

    private BigDecimal calculateTax(BigDecimal profit, InvestmentType type) {
        if (profit.compareTo(BigDecimal.valueOf(12000)) <= 0) {
            return BigDecimal.ZERO;
        }

        if (type == InvestmentType.SAVINGS_PLUS) {
            return profit.subtract(BigDecimal.valueOf(12000)).multiply(BigDecimal.valueOf(0.10));
        }

        if (type == InvestmentType.MANAGED_STOCKS) {
            BigDecimal tax = BigDecimal.ZERO;
            BigDecimal above12k = profit.subtract(BigDecimal.valueOf(12000));
            if (above12k.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal tier1 = above12k.min(BigDecimal.valueOf(28000));
                tax = tax.add(tier1.multiply(BigDecimal.valueOf(0.10)));
            }
            if (profit.compareTo(BigDecimal.valueOf(40000)) > 0) {
                BigDecimal tier2 = profit.subtract(BigDecimal.valueOf(40000));
                tax = tax.add(tier2.multiply(BigDecimal.valueOf(0.20)));
            }
            return tax;
        }

        return BigDecimal.ZERO;
    }

    private Map<InvestmentType, InvestmentMetadata> metadataByType() {
        EnumMap<InvestmentType, InvestmentMetadata> map = new EnumMap<>(InvestmentType.class);
        map.put(InvestmentType.BASIC_SAVINGS, new InvestmentMetadata(BigDecimal.valueOf(0.012), BigDecimal.valueOf(0.024), BigDecimal.valueOf(0.0025)));
        map.put(InvestmentType.SAVINGS_PLUS, new InvestmentMetadata(BigDecimal.valueOf(0.03), BigDecimal.valueOf(0.055), BigDecimal.valueOf(0.0030)));
        map.put(InvestmentType.MANAGED_STOCKS, new InvestmentMetadata(BigDecimal.valueOf(0.04), BigDecimal.valueOf(0.23), BigDecimal.valueOf(0.0130)));
        return map;
    }

    private record InvestmentMetadata(BigDecimal minAnnualReturn, BigDecimal maxAnnualReturn, BigDecimal monthlyFeeRate) {
    }
}
