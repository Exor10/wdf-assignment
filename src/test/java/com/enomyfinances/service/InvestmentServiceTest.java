package com.enomyfinances.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import com.enomyfinances.dto.InvestmentRequest;
import com.enomyfinances.dto.ProjectionResultDto;
import com.enomyfinances.entity.Investment;
import com.enomyfinances.entity.InvestmentType;
import com.enomyfinances.entity.Role;
import com.enomyfinances.entity.RoleName;
import com.enomyfinances.entity.User;
import com.enomyfinances.repository.InvestmentProjectionRepository;
import com.enomyfinances.repository.InvestmentRepository;
import com.enomyfinances.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;
    @Mock
    private InvestmentProjectionRepository projectionRepository;
    @Mock
    private UserRepository userRepository;

    private InvestmentService investmentService;

    @BeforeEach
    void setup() {
        investmentService = new InvestmentService(investmentRepository, projectionRepository, userRepository);
    }

    @Test
    void shouldCalculateProjectionForManagedStocks() {
        InvestmentRequest request = new InvestmentRequest();
        request.setInvestmentType(InvestmentType.MANAGED_STOCKS);
        request.setPrincipal(BigDecimal.valueOf(15000));

        User user = new User();
        user.setUsername("client");
        Role role = new Role();
        role.setName(RoleName.CLIENT);
        user.setRole(role);

        when(userRepository.findByUsername("client")).thenReturn(Optional.of(user));
        when(investmentRepository.save(org.mockito.ArgumentMatchers.any(Investment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<ProjectionResultDto> results = investmentService.calculateAndSave(
                request,
                new UsernamePasswordAuthenticationToken("client", "pass"));

        assertEquals(3, results.size());
        assertFalse(results.isEmpty());
    }
}
