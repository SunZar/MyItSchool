package ru.sunzar.myitschool.data;

import ru.sunzar.myitschool.data.StocksData;

public class Item {
    private StocksData.Currency currency;
    private double price;
    private String date;

    public void setPrice(float price) {
        float temp = 1 / price;
        if (currency.getApiName() == StocksData.Currency.ETH.getApiName()) {
            this.price = Math.ceil((temp * 100 + (Math.random() * temp * 100 / 100)) * 100) / 100f;
        } else {
            this.price = Math.ceil((temp + (Math.random() * temp / 100)) * 100) / 100f;
        }
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
