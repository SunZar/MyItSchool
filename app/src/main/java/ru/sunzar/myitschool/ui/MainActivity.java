package ru.sunzar.myitschool.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myitschool.R;
import com.example.myitschool.databinding.ActivityMainBinding;

import ru.sunzar.myitschool.data.ProfileData;
import ru.sunzar.myitschool.data.StocksData;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickButtonMining();

        updateCurrenciesFromShared();
        saveCurrenciesToShared();

        binding.menuNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mining:
                    onClickButtonMining();
                    break;
                case R.id.stocks:
                    onClickButtonStocks();
                    break;
                case R.id.shop:
                    onClickButtonShop();
                    break;
                case R.id.wallet:
                    onClickButtonWallet();
                    break;
                case R.id.profile:
                    onClickButtonProfile();
                    break;
            }
            return true;
        });
    }

    private void updateCurrenciesFromShared() {
        SharedPreferences preferences = getSharedPreferences("stocks_data", MODE_PRIVATE);
        for (StocksData.Currency currency : StocksData.Currency.values()) {
            StocksData.setCurrency(currency, preferences.getFloat(currency.getApiName(), 0));
        }
    }

    private void saveCurrenciesToShared() {
        final SharedPreferences.Editor preferencesProfileData = getSharedPreferences("stocks_data", MODE_PRIVATE).edit();
        for (StocksData.Currency currency : StocksData.Currency.values()) {
            StocksData.getCurrency(currency).observe(this, value -> {
                preferencesProfileData.putFloat(currency.getApiName(), value).apply();
            });
        }
        final SharedPreferences.Editor editorProfileData = getSharedPreferences("profile_data", MODE_PRIVATE).edit();
        editorProfileData.putFloat("max_of_ethereum_pref", ProfileData.max_of_ethereum);
        editorProfileData.putFloat("max_of_rubles_pref", ProfileData.max_of_rubles);
        editorProfileData.apply();
    }

    private void onClickButtonMining() {
        ClickerFragment fragment = new ClickerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onClickButtonStocks() {
        MenuStocksFragment fragment = new MenuStocksFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onClickButtonShop() {
        ShopFragment fragment = new ShopFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onClickButtonWallet() {
        WalletFragment fragment = new WalletFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void onClickButtonProfile() {
        ProfileFragment fragment = new ProfileFragment();
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