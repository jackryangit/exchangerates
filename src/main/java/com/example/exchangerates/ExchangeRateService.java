package com.example.exchangerates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExchangeRateService {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    // The URLs for the external APIs
    private static final String FRANKFURTER_API_URL = "https://api.frankfurter.app/latest?base=";
    private static final String FREE_CURRENCY_API_URL = "https://api.freecurrencyapi.com/v1/latest?apikey=API_KEY&base_currency=";

    public Map<String, Double> getExchangeRates(String baseCurrency, List<String> symbols) {
        // Fetch rates from both sources
        Map<String, Double> frankfurterRates = fetchFrankfurterRates(baseCurrency);
        Map<String, Double> freeApiRates = fetchFreeCurrencyApiRates(baseCurrency);

        // Combine the results into a single map where the key is the currency symbol (e.g., "USD")
        // and the value is a list of all rates found for that symbol (e.g., [1.07, 1.08])
        Map<String, List<Double>> collectedRates = Stream.concat(
                        frankfurterRates.entrySet().stream(),
                        freeApiRates.entrySet().stream()
                )
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        return symbols.stream()
                .filter(collectedRates::containsKey)
                .collect(Collectors.toMap(
                        symbol -> symbol,
                        symbol -> collectedRates.get(symbol).stream()
                                .mapToDouble(Double::doubleValue)
                                .average()
                                .orElse(0.0)
                ));
    }

    private Map<String, Double> fetchFrankfurterRates(String baseCurrency) {
        try {
            FrankfurterResponse response = restTemplate.getForObject(FRANKFURTER_API_URL + baseCurrency, FrankfurterResponse.class);
            if (response != null && response.rates() != null) {
                logger.info("Successfully fetched {} rates from Frankfurter.", response.rates().size());
                return response.rates();
            }
        } catch (Exception e) {
            logger.error("Could not fetch rates from Frankfurter: {}", e.getMessage());
        }
        return Collections.emptyMap();
    }

    private Map<String, Double> fetchFreeCurrencyApiRates(String baseCurrency) {
        String apiUrl = FREE_CURRENCY_API_URL.replace("API_KEY", "KEY HERE!!!") + baseCurrency;
        try {
            FreeCurrencyApiResponse response = restTemplate.getForObject(apiUrl, FreeCurrencyApiResponse.class);
            if (response != null && response.data() != null) {
                logger.info("Successfully fetched {} rates from FreeCurrencyApi.", response.data().size());
                return response.data();
            }
        } catch (Exception e) {
            logger.error("Could not fetch rates from FreeCurrencyApi: {}", e.getMessage());
        }
        return Collections.emptyMap();
    }


}
