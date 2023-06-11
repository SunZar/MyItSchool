package ru.sunzar.myitschool.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myitschool.databinding.ItemShopBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import ru.sunzar.myitschool.data.Item;
import ru.sunzar.myitschool.data.ShopData;
import ru.sunzar.myitschool.data.StocksData;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private final ArrayList<String> data_name = new ArrayList<>();
    private final ArrayList<Float> data_price = new ArrayList<>();
    private final ArrayList<Boolean> data_is_bought = new ArrayList<>();
    private boolean isBoughtNow;

    public void setData() {
        data_name.clear();
        data_price.clear();
        data_is_bought.clear();
        for (ShopData.ShopProducts value : ShopData.ShopProducts.values()) {
            data_name.add(value.getDisplayName());
            data_price.add(value.getPrice());
            data_is_bought.add(ShopData.getIsBought(value));
        }
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShopAdapter.ViewHolder(
                ItemShopBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ).getRoot()
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {
        isBoughtNow = false;
        holder.itemView.setOnClickListener(view -> {
            if (StocksData.getCurrency(StocksData.Currency.RUB).getValue() >= data_price.get(position) && ShopData.getIsBought(ShopData.getProductByIndex(position)) == false) {
                //int i = 0;
                //for (ShopData.ShopProducts products : ShopData.ShopProducts.values()) {
                //    ShopData.setIsBought(products, false);
                //    data_is_bought.add(i, false);
                //    i++;
                //}
                StocksData.setCurrency(StocksData.Currency.RUB, StocksData.getCurrency(StocksData.Currency.RUB).getValue() - data_price.get(position));
                ShopData.setIsBought(ShopData.getProductByIndex(position), true);
                Toast.makeText(view.getContext(), "Куплено! Обновите магазин для изменения данных", Toast.LENGTH_SHORT).show();
                isBoughtNow = true;
                holder.bind(data_name.get(position), data_price.get(position), data_is_bought.get(position), isBoughtNow);
                data_is_bought.add(position, isBoughtNow);
                notifyDataSetChanged();
            }
        });
        holder.bind(data_name.get(position), data_price.get(position), data_is_bought.get(position), isBoughtNow);
    }

    @Override
    public int getItemCount() {
        return data_name.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemShopBinding itemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBinding = ItemShopBinding.bind(itemView);
        }

        public void bind(String dataName, Float dataPrice, Boolean dataIsBought, boolean isBoughtNow) {
            itemBinding.name.setText(dataName);
            if (isBoughtNow == true || dataIsBought == true) {
                itemBinding.price.setText("Уже куплено");
            } else {
                itemBinding.price.setText(String.format(
                        Locale.getDefault(),
                        "%.2f",
                        dataPrice
                ) + " RUB");
            }
        }
    }
}
