package com.example.exchangerates.service;

import com.example.exchangerates.client.FrankfurterClient;
import com.example.exchangerates.client.FreeCurrencyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class ExchangeRateFetcher {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateFetcher.class);

    private final FrankfurterClient frankfurterClient;
    private final FreeCurrencyClient freeCurrencyApiClient;

    public ExchangeRateFetcher(FrankfurterClient frankfurterClient, FreeCurrencyClient freeCurrencyClient) {
        this.frankfurterClient = frankfurterClient;
        this.freeCurrencyApiClient = freeCurrencyClient;
    }

    @Cacheable(value = "frankfurterRates", key = "#baseCurrency")
    public Map<String, Double> fetchFrankfurterRates(String baseCurrency) {
        logger.info("Calling Frankfurter API for base currency: {}", baseCurrency);
        try {
            return frankfurterClient.getExchangeRates(baseCurrency);
        } catch (Exception e) {
            logger.error("Failed to fetch rates from Frankfurter: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    @Cacheable(value = "freeApiRates", key = "#baseCurrency")
    public Map<String, Double> fetchFreeApiRates(String baseCurrency) {
        logger.info("Calling FreeCurrencyAPI for base currency: {}", baseCurrency);
        try {
            return freeCurrencyApiClient.getExchangeRates(baseCurrency);
        } catch (Exception e) {
            logger.error("Failed to fetch rates from FreeCurrencyAPI: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}