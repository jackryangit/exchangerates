package com.example.exchangerates.controller;

import com.example.exchangerates.dto.ExchangeRateResponse;
import com.example.exchangerates.service.ExchangeRateService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/{baseCur}")
    public ExchangeRateResponse getExchangeRates(
            @PathVariable String baseCur,
            @RequestParam String symbols) {

        List<String> symbolList = Arrays.asList(symbols.toUpperCase().split(","));
        return exchangeRateService.getExchangeRates(baseCur.toUpperCase(), symbolList);
    }

}
