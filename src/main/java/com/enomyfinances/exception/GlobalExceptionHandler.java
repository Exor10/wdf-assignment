package com.enomyfinances.exception;

import com.enomyfinances.service.ErrorLoggingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorLoggingService errorLoggingService;

    public GlobalExceptionHandler(ErrorLoggingService errorLoggingService) {
        this.errorLoggingService = errorLoggingService;
    }

    @ExceptionHandler({ValidationException.class, CurrencyApiException.class, InvestmentCalculationException.class})
    public String handleBusiness(Exception ex, HttpServletRequest request, Model model) {
        errorLoggingService.logError("BusinessException", ex.getMessage(), request.getRequestURI());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, HttpServletRequest request, Model model) {
        errorLoggingService.logError("SystemException", ex.getMessage(), request.getRequestURI());
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        return "error";
    }
}
