package com.enomyfinances.service;

import com.enomyfinances.dto.CurrencyConversionRequest;
import com.enomyfinances.dto.CurrencyConversionResponse;
import com.enomyfinances.entity.CurrencyTransaction;
import com.enomyfinances.entity.User;
import com.enomyfinances.exception.ValidationException;
import com.enomyfinances.repository.CurrencyTransactionRepository;
import com.enomyfinances.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(300);
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(5000);

    private final ExchangeRateService exchangeRateService;
    private final CurrencyTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BigDecimal feeRate;

    public CurrencyService(ExchangeRateService exchangeRateService,
                           CurrencyTransactionRepository transactionRepository,
                           UserRepository userRepository,
                           @Value("${app.currency.transaction-fee-rate}") BigDecimal feeRate) {
        this.exchangeRateService = exchangeRateService;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.feeRate = feeRate;
    }

    public CurrencyConversionResponse convert(CurrencyConversionRequest request, Authentication authentication) {
        validateAmount(request.getAmount());

        BigDecimal rate = exchangeRateService.getRate(request.getSourceCurrency(), request.getTargetCurrency());
        BigDecimal converted = request.getAmount().multiply(rate).setScale(4, RoundingMode.HALF_UP);
        BigDecimal fee = converted.multiply(feeRate).setScale(4, RoundingMode.HALF_UP);
        BigDecimal finalAmount = converted.subtract(fee).setScale(4, RoundingMode.HALF_UP);

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ValidationException("Authenticated user not found"));

        CurrencyTransaction tx = new CurrencyTransaction();
        tx.setUser(user);
        tx.setSourceCurrency(request.getSourceCurrency());
        tx.setTargetCurrency(request.getTargetCurrency());
        tx.setAmount(request.getAmount());
        tx.setExchangeRate(rate);
        tx.setConvertedAmount(converted);
        tx.setFee(fee);
        tx.setFinalAmount(finalAmount);
        tx.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(tx);

        log.info("Currency transaction created for user={}, from={} to={}, amount={}",
                user.getUsername(), request.getSourceCurrency(), request.getTargetCurrency(), request.getAmount());

        return new CurrencyConversionResponse(rate, converted, fee, finalAmount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(MIN_AMOUNT) < 0) {
            log.warn("Invalid transaction: amount lower than minimum: {}", amount);
            throw new ValidationException("Minimum transaction amount is 300");
        }
        if (amount.compareTo(MAX_AMOUNT) > 0) {
            log.warn("Invalid transaction: amount higher than maximum: {}", amount);
            throw new ValidationException("Maximum transaction amount is 5000");
        }
    }
}
