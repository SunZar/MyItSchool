package com.example.myitschool;

import java.util.ArrayList;
import java.util.List;

public class StocksSearchResponse {
    public List<StocksShortData> getSearch() {
        return search;
    }

    private final List<StocksShortData> search;

    public StocksSearchResponse() {
        this(new ArrayList<>());
    }

    public StocksSearchResponse(List<StocksShortData> search) {
        this.search = search;
    }
}
