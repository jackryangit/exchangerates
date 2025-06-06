package com.example.exchangerates.client;

import com.example.exchangerates.client.dto.FreeCurrencyResponse;
import com.example.exchangerates.service.MetricsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class FreeCurrencyClient {
    private static final String API_NAME = "freeCurrencyApi";
    private static final String API_URL = "https://api.freecurrencyapi.com/v1/latest?apikey=API_KEY&base_currency=";
    private final RestTemplate restTemplate;
    private final MetricsService metricsService;

    public FreeCurrencyClient(RestTemplate restTemplate, MetricsService metricsService) {
        this.metricsService = metricsService;
        this.restTemplate = restTemplate;
    }

    public Map<String, Double> getExchangeRates(String baseCurrency) {
        metricsService.incrementApiRequests(API_NAME);
        String url = API_URL.replace("API_KEY", "fca_live_...") + baseCurrency;
        FreeCurrencyResponse response = restTemplate.getForObject(url, FreeCurrencyResponse.class);
        if (response != null && response.getData() != null) {
            metricsService.incrementApiResponses(API_NAME);
            return response.getData();
        }
        return Map.of();
    }

}

