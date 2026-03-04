package com.enomyfinances.repository;

import com.enomyfinances.entity.CurrencyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyTransactionRepository extends JpaRepository<CurrencyTransaction, Long> {
}
