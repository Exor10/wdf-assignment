package com.enomyfinances.repository;

import com.enomyfinances.entity.InvestmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentProjectionRepository extends JpaRepository<InvestmentProjection, Long> {
}
