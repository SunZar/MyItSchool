package ru.sunzar.myitschool.data;

import java.util.List;

public class StockGraphData {
    private final List<String> labels;
    private final List<Float> values;
    private final float currentPrice;

    public StockGraphData(List<String> labels, List<Float> values, float currentPrice) {
        this.labels = labels;
        this.values = values;
        this.currentPrice = currentPrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public List<Float> getValues() {
        return values;
    }

    public List<String> getLabels() {
        return labels;
    }
}
