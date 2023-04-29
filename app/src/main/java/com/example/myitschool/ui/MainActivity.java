package com.example.myitschool.ui;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.myitschool.data.StocksRepository;
import com.example.myitschool.data.StocksSearchResponse;
import com.example.myitschool.databinding.ActivityMainBinding;
import com.example.myitschool.utils.Resource;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String moneyStr;
    private int money;

    private int levelUpgradeAddMoney = 1;
    private int levelUpgradeOfflineMoney = 0;

    private int addMoney = 1;
    private int offlineMoney = 0;
    private float offlineTime = 10f;

    SharedPreferences mySP;
    final int SAVE_MONEY = 0;
    final int SAVE_LEVEL_UPGRADE_ADD_MONEY = 1;
    final int SAVE_LEVEL_UPGRADE_OFFLINE_MONEY = 2;
    final int SAVE_OFFLINE_MONEY = 3;
    final int SAVE_ADD_MONEY = 4;
    final int SAVE_LEVEL_UPGRADE_OFFLINE_TIME = 5;

    private Handler offlineHandler;



    private final StocksRepository repository = new StocksRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        StocksFragment stocksFragment = new StocksFragment();
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.rootContainer, stocksFragment)
//                .commit();

        binding.stocks.setOnClickListener(view -> {
            goSearchStocks();
        });

        repository.stockSearchLiveData.observe(this, stocksSearchResponseResource -> {
            Log.d("LOG", "RESPONSE SUCCESS: " + stocksSearchResponseResource.toString());
            if (stocksSearchResponseResource instanceof Resource.Success) {
                onUpdateData((Resource.Success<StocksSearchResponse>)stocksSearchResponseResource);
            } else if (stocksSearchResponseResource instanceof Resource.Error) {
                showError();
            } else if (stocksSearchResponseResource instanceof Resource.Loading) {
                showLoading(); //Не забудь скрыть загрузку в других состояниях
            }
        });
    }

    private void showLoading() {

    }

    private void showError() {

    }

    private void onUpdateData(Resource.Success<StocksSearchResponse> state) {
        state.getValue().getCurrencies().forEach((currency, aDouble) -> {
            Log.d("LOG", currency + ": " + aDouble);
        });
    }


    private void goSearchStocks() {
        String searchRequest = binding.currency.getText().toString().trim();
        repository.search(searchRequest);
    }
}