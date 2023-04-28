package com.example.myitschool;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.myitschool.databinding.ActivityMainBinding;

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
        StocksFragment stocksFragment = new StocksFragment();
        
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootContainer, stocksFragment)
                .commit();

        repository.setOnLoadingStocksState(state -> {
            if (state instanceof OnLoadingStocksState.State.Success) {
                onUpdateData((OnLoadingStocksState.State.Success) state);
            }
        });
    }

    private void onUpdateData(OnLoadingStocksState.State.Success state) {
        //adapter.setItems(state.getStocks());
    }


    private void goSearchStocks() {
        //String searchRequest = binding.search.getText().toString();
        //repository.search(searchRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repository.removeOnLoadingStocksState();
    }
}