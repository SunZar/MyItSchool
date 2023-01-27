package com.example.myitschool;

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

    SharedPreferences mySP;
    final int SAVE_INT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyStr = binding.moneyCount.getText().toString();
                money = Integer.parseInt(moneyStr) + 1;
                moneyStr = Integer.toString(money);
                binding.moneyCount.setText(moneyStr);
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

    private void savePref() {
        mySP = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mySP.edit();
        editor.putInt(String.valueOf(SAVE_INT), money);
        editor.commit();
        Toast.makeText(this, "Progress saved", Toast.LENGTH_SHORT).show();
    }

    private void loadPref() {
        mySP = getPreferences(MODE_PRIVATE);
        int saveInt = mySP.getInt(String.valueOf(SAVE_INT), 0);
        binding.moneyCount.setText(Integer.toString(saveInt));
        Toast.makeText(this, "Progress loaded", Toast.LENGTH_SHORT).show();
    }
}