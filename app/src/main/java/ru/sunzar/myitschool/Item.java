package ru.sunzar.myitschool;

import ru.sunzar.myitschool.data.StocksData;

public class Item {
    private StocksData.Currency currency;
    private double price;
    private String date;

    public void setPrice(float price) {
        float temp = 1 / price;
        this.price = Math.ceil((temp + (Math.random() * temp / 100)) * 100) / 100f;
    }

    public Item(StocksData.Currency currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public StocksData.Currency getCurrency() {
        return currency;
    }
}
