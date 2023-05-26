package ru.sunzar.myitschool.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myitschool.databinding.FragmentShopBinding;

import java.util.ArrayList;

import ru.sunzar.myitschool.data.ShopData;
import ru.sunzar.myitschool.data.StocksData;

public class ShopFragment extends ToolbarBaseFragment {
    private FragmentShopBinding binding;

    private final ShopAdapter adapter = new ShopAdapter();

    private final ArrayList<String> data_name = new ArrayList<String>();
    private final ArrayList<Float> data_price = new ArrayList<Float>();
    private final ArrayList<Boolean> data_is_bought = new ArrayList<Boolean>();

    @Override
    public View onCreateChildView(@NonNull ViewGroup parent, @Nullable Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.container.setAdapter(adapter);
        baseBinding.title.setText("Магазин");

        binding.updateShop.setOnClickListener(view1 -> {
            loadShopData();
        });

        updateCurrenciesFromShared();
        saveCurrenciesToShared();
        loadShopData();
    }

    private void loadShopData() {
        data_name.clear();
        data_price.clear();
        data_is_bought.clear();
        for (ShopData.ShopProducts value : ShopData.ShopProducts.values()) {
            data_name.add(value.getDisplayName());
            data_price.add(value.getPrice());
        }
        for (ShopData.ShopProducts value : ShopData.ShopProducts.values()) {
            data_is_bought.add(ShopData.getIsBought(value));
        }
        adapter.setData(data_name, data_price, data_is_bought);
        adapter.notifyDataSetChanged();
    }

    private void updateCurrenciesFromShared() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("shop_data", MODE_PRIVATE);
        for (ShopData.ShopProducts products : ShopData.ShopProducts.values()) {
            ShopData.setIsBought(products, preferences.getBoolean(products.getDisplayName(), false));
        }
    }

    private void saveCurrenciesToShared() {
        final SharedPreferences.Editor preferences = this.getActivity().getSharedPreferences("shop_data", MODE_PRIVATE).edit();
        for (ShopData.ShopProducts products : ShopData.ShopProducts.values()) {
            preferences.putBoolean(products.getDisplayName(), ShopData.getIsBought(products));
        }
        preferences.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveCurrenciesToShared();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveCurrenciesToShared();
    }
}