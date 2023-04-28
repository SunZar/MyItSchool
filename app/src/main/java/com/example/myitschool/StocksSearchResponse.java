package com.example.myitschool;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StocksSearchResponse {
    @SerializedName("usd")
    private final List<StocksShortData> usd;

    public List<StocksShortData> getSearch() {
        return usd;
    }

    public StocksSearchResponse() {
        this(new ArrayList<>());
    }

    public StocksSearchResponse(List<StocksShortData> search) {
        this.usd = search;
    }
}
