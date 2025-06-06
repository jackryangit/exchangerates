package com.example.exchangerates.controller;

import com.example.exchangerates.dto.ExchangeRateResponse;
import com.example.exchangerates.service.ExchangeRateService;
import com.example.exchangerates.service.MetricsService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;
    private final MetricsService metricsService;

    public ExchangeRateController(ExchangeRateService exchangeRateService, MetricsService metricsService) {
        this.exchangeRateService = exchangeRateService;
        this.metricsService = metricsService;
    }

    @GetMapping("/{baseCur}")
    public ExchangeRateResponse getExchangeRates(
            @PathVariable String baseCur,
            @RequestParam String symbols) {

        metricsService.incrementTotalQueries();
        List<String> symbolList = Arrays.asList(symbols.toUpperCase().split(","));
        return exchangeRateService.getExchangeRates(baseCur.toUpperCase(), symbolList);
    }

}
