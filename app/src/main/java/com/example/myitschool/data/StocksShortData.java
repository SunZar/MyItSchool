package com.example.myitschool.data;

import com.google.gson.annotations.SerializedName;

public class StocksShortData {
    @SerializedName("usd")
    private final String usd;

    public StocksShortData() {
        this("Test");
    }

    public StocksShortData(String usd) {
        this.usd = usd;
    }
}
