package com.example.exchangerates.service;

import com.example.exchangerates.dto.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock // Create a fake ExchangeRateFetcher
    private ExchangeRateFetcher exchangeRateFetcher;

    @InjectMocks // Create an ExchangeRateService and inject the fake fetcher into it
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        String baseCurrency = "EUR";
        List<String> symbols = List.of("USD", "GBP");

        // When fetchFrankfurterRates is called, return this map
        when(exchangeRateFetcher.fetchFrankfurterRates(baseCurrency))
                .thenReturn(Map.of("USD", 1.10, "GBP", 1.2));

        // When fetchFreeApiRates is called, return this map
        when(exchangeRateFetcher.fetchFreeApiRates(baseCurrency))
                .thenReturn(Map.of("USD", 1.20, "GBP", 8.0));
    }

    @Test
    void getExchangeRates_shouldReturnCorrectlyAveragedRates() {
        ExchangeRateResponse response = exchangeRateService.getExchangeRates("EUR", List.of("USD", "GBP"));

        // Verify the results of the averaging logic
        assertNotNull(response);
        assertEquals("EUR", response.getBase());
        assertEquals(2, response.getRates().size());
        assertEquals(1.15, response.getRates().get("USD")); // Average of 1.10 and 1.20
        assertEquals(4.6, response.getRates().get("GBP")); // Average of 1.2 and 8.0
    }
}
