package com.enomyfinances.controller;

import com.enomyfinances.dto.InvestmentRequest;
import com.enomyfinances.dto.ProjectionResultDto;
import com.enomyfinances.entity.InvestmentType;
import com.enomyfinances.service.InvestmentService;
import com.enomyfinances.util.CurrencyFormatter;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @GetMapping("/investments")
    public String form(Model model) {
        model.addAttribute("request", new InvestmentRequest());
        model.addAttribute("types", InvestmentType.values());
        return "investment-form";
    }

    @PostMapping("/investments/project")
    public String project(@Valid @ModelAttribute("request") InvestmentRequest request,
                          BindingResult bindingResult,
                          Authentication authentication,
                          Model model) {
        model.addAttribute("types", InvestmentType.values());
        if (bindingResult.hasErrors()) {
            return "investment-form";
        }
        List<ProjectionResultDto> results = investmentService.calculateAndSave(request, authentication);
        model.addAttribute("results", results);
        model.addAttribute("currencyFormatter", CurrencyFormatter.class);
        return "investment-result";
    }
}
