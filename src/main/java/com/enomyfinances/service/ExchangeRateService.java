package com.enomyfinances.service;

import com.enomyfinances.entity.CurrencyCode;
import com.enomyfinances.exception.CurrencyApiException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final long ttlSeconds;

    private final Map<CurrencyCode, CacheEntry> ratesCache = new ConcurrentHashMap<>();

    public ExchangeRateService(RestTemplate restTemplate,
                               @Value("${app.exchange-rate.api-url}") String apiUrl,
                               @Value("${app.exchange-rate.cache-ttl-seconds}") long ttlSeconds) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.ttlSeconds = ttlSeconds;
    }

    public BigDecimal getRate(CurrencyCode source, CurrencyCode target) {
        if (source == target) {
            return BigDecimal.ONE;
        }

        CacheEntry entry = ratesCache.get(source);
        if (entry != null && Duration.between(entry.timestamp(), Instant.now()).getSeconds() < ttlSeconds) {
            BigDecimal cachedRate = entry.rates().get(target);
            if (cachedRate != null) {
                return cachedRate;
            }
        }

        try {
            ApiRateResponse response = restTemplate.getForObject(apiUrl + source.name(), ApiRateResponse.class);
            if (response == null || response.conversion_rates() == null) {
                throw new CurrencyApiException("Exchange API response is invalid");
            }

            EnumMap<CurrencyCode, BigDecimal> mappedRates = new EnumMap<>(CurrencyCode.class);
            response.conversion_rates().forEach((k, v) -> {
                try {
                    mappedRates.put(CurrencyCode.valueOf(k), BigDecimal.valueOf(v));
                } catch (IllegalArgumentException ignored) {
                    // Skip unsupported currencies
                }
            });
            ratesCache.put(source, new CacheEntry(mappedRates, Instant.now()));
            BigDecimal freshRate = mappedRates.get(target);
            if (freshRate == null) {
                throw new CurrencyApiException("Rate unavailable for target currency");
            }
            return freshRate;
        } catch (Exception ex) {
            log.error("Exchange rate API failed for {}->{}, using fallback cache if available", source, target, ex);
            if (entry != null && entry.rates().get(target) != null) {
                return entry.rates().get(target);
            }
            throw new CurrencyApiException("Unable to retrieve exchange rates currently", ex);
        }
    }

    private record CacheEntry(Map<CurrencyCode, BigDecimal> rates, Instant timestamp) {
    }

    private record ApiRateResponse(Map<String, Double> conversion_rates) {
    }
}
