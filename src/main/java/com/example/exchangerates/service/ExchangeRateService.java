package com.example.exchangerates.service;

import com.example.exchangerates.dto.ExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExchangeRateService {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    private final ExchangeRateFetcher exchangeRateFetcher;

    public ExchangeRateService(ExchangeRateFetcher exchangeRateFetcher) {
        this.exchangeRateFetcher = exchangeRateFetcher;
    }

    public ExchangeRateResponse getExchangeRates(String baseCurrency, List<String> symbols) {
        logger.info("Fetching rates for base {} and symbols: {}", baseCurrency, symbols);
        // Fetch rates from both sources
        Map<String, Double> frankfurterRates = exchangeRateFetcher.fetchFrankfurterRates(baseCurrency);
        Map<String, Double> freeApiRates = exchangeRateFetcher.fetchFreeApiRates(baseCurrency);

        Map<String, Double> averagedRates = averageRates(frankfurterRates, freeApiRates, symbols);

        return new ExchangeRateResponse( baseCurrency, averagedRates);
    }

    private Map<String, Double> averageRates(Map<String, Double> rates1, Map<String, Double> rates2, List<String> symbols) {
        Map<String, List<Double>> collectedRates = Stream.concat(rates1.entrySet().stream(),
                        rates2.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        return symbols.stream()
                .filter(collectedRates::containsKey)
                .collect(Collectors.toMap(
                        symbol -> symbol,
                        symbol -> {
                            List<Double> values = collectedRates.get(symbol);
                            return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                        }
                ));
    }


}
