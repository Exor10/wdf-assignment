package com.enomyfinances.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.enomyfinances.dto.CurrencyConversionRequest;
import com.enomyfinances.dto.CurrencyConversionResponse;
import com.enomyfinances.entity.CurrencyCode;
import com.enomyfinances.entity.Role;
import com.enomyfinances.entity.RoleName;
import com.enomyfinances.entity.User;
import com.enomyfinances.exception.ValidationException;
import com.enomyfinances.repository.CurrencyTransactionRepository;
import com.enomyfinances.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private ExchangeRateService exchangeRateService;
    @Mock
    private CurrencyTransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;

    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(exchangeRateService, transactionRepository, userRepository, BigDecimal.valueOf(0.015));
    }

    @Test
    void shouldConvertCurrencySuccessfully() {
        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setSourceCurrency(CurrencyCode.GBP);
        request.setTargetCurrency(CurrencyCode.USD);
        request.setAmount(BigDecimal.valueOf(500));

        User user = new User();
        user.setUsername("client");
        Role role = new Role();
        role.setName(RoleName.CLIENT);
        user.setRole(role);

        when(exchangeRateService.getRate(CurrencyCode.GBP, CurrencyCode.USD)).thenReturn(BigDecimal.valueOf(1.2));
        when(userRepository.findByUsername("client")).thenReturn(Optional.of(user));

        CurrencyConversionResponse response = currencyService.convert(request,
                new UsernamePasswordAuthenticationToken("client", "pass"));

        assertEquals(new BigDecimal("600.0000"), response.getConverted());
        assertEquals(new BigDecimal("9.0000"), response.getFee());
        assertEquals(new BigDecimal("591.0000"), response.getFinalAmount());
    }

    @Test
    void shouldThrowValidationWhenAmountBelowMinimum() {
        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setSourceCurrency(CurrencyCode.GBP);
        request.setTargetCurrency(CurrencyCode.EUR);
        request.setAmount(BigDecimal.valueOf(200));

        assertThrows(ValidationException.class, () -> currencyService.convert(request,
                new UsernamePasswordAuthenticationToken("client", "pass")));
    }
}
