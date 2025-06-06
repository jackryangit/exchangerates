package com.example.exchangerates.client;

import com.example.exchangerates.client.dto.FrankfurterResponse;
import com.example.exchangerates.service.MetricsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class FrankfurterClient {
    private static final String API_NAME = "frankfurter";
    private static final String API_URL = "https://api.frankfurter.app/latest?base=";
    private final RestTemplate restTemplate;
    private final MetricsService metricsService;

    public FrankfurterClient(MetricsService metricsService) {
        this.restTemplate = new RestTemplate();
        this.metricsService = metricsService;
    }

    public Map<String, Double> getExchangeRates(String baseCurrency) {
        metricsService.incrementApiRequests(API_NAME);
        String url = API_URL + baseCurrency;
        FrankfurterResponse response = restTemplate.getForObject(url, FrankfurterResponse.class);
        if (response != null && response.getRates() != null) {
            metricsService.incrementApiResponses(API_NAME);
            return response.getRates();
        }
        return Map.of();
    }

}
