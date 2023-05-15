package com.example.myitschool.data;

import java.util.List;

public class StockGraphData {
    private final List<String> labels;
    private final List<Double> values;
    private final double currentPrice;

    public StockGraphData(List<String> labels, List<Double> values, double currentPrice) {
        this.labels = labels;
        this.values = values;
        this.currentPrice = currentPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public List<Double> getValues() {
        return values;
    }

    public List<String> getLabels() {
        return labels;
    }
}
