package ru.sunzar.myitschool.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myitschool.R;
import com.example.myitschool.databinding.ActivityMainBinding;

import ru.sunzar.myitschool.ClickerActivity;
import ru.sunzar.myitschool.MenuStocksFragment;
import ru.sunzar.myitschool.ShopFragment;
import ru.sunzar.myitschool.WalletFragment;
import ru.sunzar.myitschool.data.StocksData;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pasteMenuStocksFragment();

        binding.mining.setOnClickListener(view -> {
            onClickButtonMining();
        });

        binding.stocks.setOnClickListener(view -> {
            onClickButtonStocks();
        });

        binding.shop.setOnClickListener(view -> {
            onClickButtonShop();
        });

        binding.wallet.setOnClickListener(view -> {
            onClickButtonWallet();
        });
        updateCurrenciesFromShared();
        saveCurrenciesToShared();
    }

    private void updateCurrenciesFromShared() {
        SharedPreferences preferences = getSharedPreferences("stocks_data", MODE_PRIVATE);
        for (StocksData.Currency currency : StocksData.Currency.values()) {
            StocksData.setCurrency(currency, preferences.getFloat(currency.getApiName(), 0));
        }
    }

    private void saveCurrenciesToShared() {
        final SharedPreferences.Editor preferences = getSharedPreferences("stocks_data", MODE_PRIVATE).edit();
        for (StocksData.Currency currency : StocksData.Currency.values()) {
            StocksData.getCurrency(currency).observe(this, value -> {
                preferences.putFloat(currency.getApiName(), value).apply();
            });
        }
    }

    private void onClickButtonMining() {
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.textShadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
        ClickerActivity fragment = new ClickerActivity();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onClickButtonStocks() {
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.textShadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
        MenuStocksFragment fragment = new MenuStocksFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onClickButtonShop() {
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.textShadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
        ShopFragment fragment = new ShopFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onClickButtonWallet() {
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
//        binding.mining.setBackgroundTintList(getResources().getColorStateList(R.color.textShadow));
//        binding.mining.setTextColor(getResources().getColorStateList(R.color.textDarkShadow));
        WalletFragment fragment = new WalletFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void pasteMenuStocksFragment() {
        MenuStocksFragment fragment = new MenuStocksFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void pasteStocksFragment(Bundle bundle) {
        MenuStocksFragment fragment = new MenuStocksFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public Context getCont() {
        return this;
    }
}