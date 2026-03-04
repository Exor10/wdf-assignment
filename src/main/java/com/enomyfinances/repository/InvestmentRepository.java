package com.enomyfinances.repository;

import com.enomyfinances.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
}
