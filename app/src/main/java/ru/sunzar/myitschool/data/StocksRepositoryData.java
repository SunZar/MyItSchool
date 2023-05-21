package ru.sunzar.myitschool.data;

import java.util.ArrayList;
import java.util.List;

import ru.sunzar.myitschool.data.Item;
import ru.sunzar.myitschool.data.StocksData;

public class StocksRepositoryData {
    private final ArrayList<Item> items = new ArrayList<>();

    public StocksRepositoryData() {
        for (StocksData.Currency currency : StocksData.Currency.values()) {
            items.add(new Item(currency));
        }
    }

    public List<Item> getItems() {
        return (ArrayList<Item>) items.clone();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public void setItems(ArrayList<Item> itemsTemp) {
        items.clear();
        items.addAll(itemsTemp);
    }
}
