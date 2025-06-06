package com.example.exchangerates;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/exchange-rates/{baseCur}")
    public ExchangeRateResponse getExchangeRates(
            @PathVariable String baseCur,
            @RequestParam String symbols) {

        List<String> symbolList = Arrays.asList(symbols.toUpperCase().split(","));
        Map<String, Double> rates = exchangeRateService.getExchangeRates(baseCur, symbolList);

        return new ExchangeRateResponse(baseCur.toUpperCase(), rates);
    }

}
