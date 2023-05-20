package ru.sunzar.myitschool.data;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class StocksSearchResponse {
    @SerializedName("date")
    private final String date;
    @SerializedName("rub")
    private final Map<String, Float> currencies;

    public StocksSearchResponse(String date, Map<String, Float> currencies) {
        this.date = date;
        this.currencies = currencies;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Float> getCurrencies() {
        return currencies;
    }
}
