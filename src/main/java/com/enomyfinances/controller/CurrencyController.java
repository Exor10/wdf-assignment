package com.enomyfinances.controller;

import com.enomyfinances.dto.CurrencyConversionRequest;
import com.enomyfinances.dto.CurrencyConversionResponse;
import com.enomyfinances.entity.CurrencyCode;
import com.enomyfinances.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currency")
    public String form(Model model) {
        model.addAttribute("request", new CurrencyConversionRequest());
        model.addAttribute("currencies", CurrencyCode.values());
        return "currency-form";
    }

    @PostMapping("/currency/convert")
    public String convert(@Valid @ModelAttribute("request") CurrencyConversionRequest request,
                          BindingResult bindingResult,
                          Authentication authentication,
                          Model model) {
        model.addAttribute("currencies", CurrencyCode.values());
        if (bindingResult.hasErrors()) {
            return "currency-form";
        }
        CurrencyConversionResponse response = currencyService.convert(request, authentication);
        model.addAttribute("result", response);
        model.addAttribute("request", request);
        return "currency-result";
    }
}
