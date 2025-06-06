package com.example.exchangerates.client.dto;

import java.util.Map;

public class FreeCurrencyResponse{
    private Map<String, Double> data;

    public Map<String, Double> getData() { return data; }
    public void setData(Map<String, Double> data) { this.data = data; }
}
