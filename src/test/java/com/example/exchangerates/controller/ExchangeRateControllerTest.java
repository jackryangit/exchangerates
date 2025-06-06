package com.example.exchangerates.controller;

import com.example.exchangerates.dto.ExchangeRateResponse;
import com.example.exchangerates.service.ExchangeRateService;
import com.example.exchangerates.service.MetricsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ExchangeRateController.class)
class ExchangeRateControllerTest {

    // MockMvc allows us to perform fake HTTP requests against our controller
    @Autowired
    private MockMvc mockMvc;

    // @MockBean creates a mock of the ExchangeRateService and adds it to the application context.
    // The real service is never created; the controller will be wired with this fake one.
    @MockBean
    private ExchangeRateService exchangeRateService;

    @MockBean
    private MetricsService metricsService;

    @Test
    void getExchangeRates_whenValidRequest_shouldReturn200AndCorrectData() throws Exception {
        String baseCurrency = "EUR";
        List<String> symbols = List.of("USD", "NZD");

        // Create the fake response object we want our mock service to return.
        Map<String, Double> fakeRates = Map.of("USD", 1.07, "NZD", 1.79);
        ExchangeRateResponse fakeServiceResponse = new ExchangeRateResponse(baseCurrency, fakeRates);

        // When exchangeRateService.getExchangeRates is called with these arguments...
        when(exchangeRateService.getExchangeRates(baseCurrency, symbols))
                .thenReturn(fakeServiceResponse); // ...then return our fake response object.

        // Perform a fake request and verify the results.
        mockMvc.perform(get("/exchangeRates/{baseCur}?symbols=USD,NZD", baseCurrency))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("EUR"))
                .andExpect(jsonPath("$.rates.USD").value(1.07))
                .andExpect(jsonPath("$.rates.NZD").value(1.79));

        // Verify that the controller did in fact call our service
        verify(exchangeRateService).getExchangeRates(baseCurrency, symbols);
    }

    @Test
    void getExchangeRates_whenRequestParamIsMissing_shouldReturnBadRequest() throws Exception {
        // Perform an invalid request
        mockMvc.perform(get("/exchangeRates/EUR"))
                .andExpect(status().isBadRequest()); // Expect an HTTP 400 Bad Request status
    }
}