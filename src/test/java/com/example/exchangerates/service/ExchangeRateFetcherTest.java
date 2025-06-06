package com.example.exchangerates.service;

import com.example.exchangerates.client.FrankfurterClient;
import com.example.exchangerates.client.FreeCurrencyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateFetcherTest {

    @Mock
    private FrankfurterClient frankfurterClient;
    @Mock
    private FreeCurrencyClient freeCurrencyClient;
    @Mock
    private MetricsService metricsService;

    @InjectMocks
    private ExchangeRateFetcher exchangeRateFetcher;

    @BeforeEach
    void setUp() {
        when(frankfurterClient.getExchangeRates("EUR"))
                .thenReturn(Map.of("USD", 1.1));
    }

    @Test
    void fetchFrankfurterRates_shouldReturnRates_whenClientSucceeds() {
        Map<String, Double> actualRates = exchangeRateFetcher.fetchFrankfurterRates("EUR");
        assertEquals(1, actualRates.size());
        assertEquals(1.1, actualRates.get("USD"));
    }

    @Test
    void fetchFrankfurterRates_shouldReturnEmptyMap_whenClientFails() {
        when(frankfurterClient.getExchangeRates("EUR")).thenThrow(new RuntimeException("API is down"));

        Map<String, Double> actualRates = exchangeRateFetcher.fetchFrankfurterRates("EUR");
        assertTrue(actualRates.isEmpty());
    }
}
