package com.example.exchangerates;

import java.util.Map;

public record FreeCurrencyApiResponse(Map<String, Double> data) { }
