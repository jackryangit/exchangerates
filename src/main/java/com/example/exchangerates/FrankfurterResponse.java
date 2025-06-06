package com.example.exchangerates;

import java.util.Map;

public record FrankfurterResponse(String base, Map<String, Double> rates) { }
