package com.example.exchangerates;

import java.util.Map;

public record ExchangeRateResponse(String base, Map<String, Double> rates) {}
