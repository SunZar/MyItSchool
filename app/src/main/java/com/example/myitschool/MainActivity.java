package com.example.myitschool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myitschool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String moneyStr;
    private int money;

    private int levelUpgrade = 1;

    private int addMoney = 1;

    SharedPreferences mySP;
    final int SAVE_MONEY = 0;
    final int SAVE_LEVEL_UPGRADE = 1;
    final int SAVE_ADD_MONEY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_LEVEL_UPGRADE), 1) != 1) {
            mySP = getPreferences(MODE_PRIVATE);
            int saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE), 1);
            levelUpgrade = saveInt;
        }

        if (getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_ADD_MONEY), 1) != 1) {
            mySP = getPreferences(MODE_PRIVATE);
            int saveInt = mySP.getInt(String.valueOf(SAVE_ADD_MONEY), 1);
            addMoney = saveInt;
        }

        if (getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_MONEY), 0) != 0) {
            mySP = getPreferences(MODE_PRIVATE);
            int saveInt = mySP.getInt(String.valueOf(SAVE_MONEY), 0);
            binding.moneyCount.setText(Integer.toString(saveInt));
        }
        if (savedInstanceState != null) {
            binding.moneyCount.setText(Integer.toString(savedInstanceState.getInt("money")));
            levelUpgrade = savedInstanceState.getInt("levelUpgrade");
            addMoney = savedInstanceState.getInt("addMoney");
        }
        binding.upgradePrice.setText("Price: " + Integer.toString(addMoney * 20));

        binding.buttonMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyStr = binding.moneyCount.getText().toString();
                money = Integer.parseInt(moneyStr) + addMoney;
                moneyStr = Integer.toString(money);
                binding.moneyCount.setText(moneyStr);
            }
        });

        binding.buttonUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(binding.moneyCount.getText().toString()) >= addMoney * 20) {
                    binding.moneyCount.setText(
                            Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) - addMoney * 20)
                    );
                    levelUpgrade = levelUpgrade + 1;
                    addMoney = addMoney + 1;
                    binding.upgradePrice.setText("Price: " + Integer.toString(addMoney * 20));
                } else {
                    notEnoughMoney();
                }
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePref();
            }
        });

        binding.buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPref();
            }
        });

    }

    private void notEnoughMoney() {
        Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
    }

    private void savePref() {
        mySP = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mySP.edit();

        editor.putInt(String.valueOf(SAVE_MONEY), money);
        editor.putInt(String.valueOf(SAVE_LEVEL_UPGRADE), levelUpgrade);
        editor.putInt(String.valueOf(SAVE_ADD_MONEY), addMoney);

        editor.commit();
        Toast.makeText(this, "Progress saved", Toast.LENGTH_SHORT).show();
    }

    private void loadPref() {
        mySP = getPreferences(MODE_PRIVATE);
        int saveInt = mySP.getInt(String.valueOf(SAVE_MONEY), 0);
        binding.moneyCount.setText(Integer.toString(saveInt));
        Toast.makeText(this, "Progress loaded", Toast.LENGTH_SHORT).show();
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("money", Integer.parseInt(binding.moneyCount.getText().toString()));
        outState.putInt("levelUpgrade", levelUpgrade);
        outState.putInt("addMoney", addMoney);
    }

}