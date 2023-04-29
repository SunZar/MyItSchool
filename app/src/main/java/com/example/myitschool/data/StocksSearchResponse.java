package com.example.myitschool.data;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class StocksSearchResponse {
    @SerializedName("date")
    private final String date;
    @SerializedName("usd")
    private final Map<String, Double> currencies;

    public StocksSearchResponse(String date, Map<String, Double> currencies) {
        this.date = date;
        this.currencies = currencies;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Double> getCurrencies() {
        return currencies;
    }
}
