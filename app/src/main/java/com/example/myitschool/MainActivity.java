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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        loadGetAllPref();

        //Loading all savedInstanceState
        if (savedInstanceState != null) {

            binding.moneyCount.setText(Integer.toString(savedInstanceState.getInt("money")));
            binding.upgradeOfflineMoneyPrice.setText("Price: " + Integer.toString((offlineMoney + 1) * 200));
            binding.upgradeAddMoneyPrice.setText("Price: " + Integer.toString(addMoney * 20));

            levelUpgradeAddMoney = savedInstanceState.getInt("levelUpgradeAddMoney");
            levelUpgradeOfflineMoney = savedInstanceState.getInt("levelUpgradeOfflineMoney");
            offlineMoney = savedInstanceState.getInt("offlineMoney");
            addMoney = savedInstanceState.getInt("addMoney");
            offlineTime = savedInstanceState.getFloat("offlineTime");
        }
        binding.upgradeAddMoneyPrice.setText("Price: " + Integer.toString(addMoney * 20));
        binding.upgradeOfflineMoneyPrice.setText("Price: " + Integer.toString((offlineMoney + 1) * 200));
        binding.addMoneyCount.setText("Money per\nclick: " + Integer.toString(addMoney));
        binding.addOfflineMoneyCount.setText("Money per\n" + offlineTime + ": " + Integer.toString(offlineMoney));

        //Checking all buttons for click
        binding.buttonMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyStr = binding.moneyCount.getText().toString();
                money = Integer.parseInt(moneyStr) + addMoney;
                moneyStr = Integer.toString(money);
                binding.moneyCount.setText(moneyStr);
            }
        });

        binding.buttonUpgradeAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnUpgradeAddMoney();
            }
        });

        binding.buttonUpgradeOfflineMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnUpgradeOfflineMoney();
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

        binding.buttonUpgradeOfflineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnUpgradeOfflineTime();
            }
        });

        offlineHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                binding.moneyCount.setText(
                        Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) + offlineMoney)
                );
                offlineHandler.sendEmptyMessageDelayed(0, (long) (offlineTime * 1000));
            }
        };

        offlineHandler.sendEmptyMessage(0);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    offlineHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            binding.moneyCount.setText(
//                                    Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) + offlineMoney + 1)
//                            );
//                        }
//                    });
//                }
//            }
//        });

//        OfflineMoney offlineMoney = new OfflineMoney();
//        offlineMoney.start();
//
//        binding.moneyCount.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.moneyCount.setText(
//                        Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) + offlineMoney + 1)
//                );
//            }
//        }, 10000);

    }

//    class OfflineMoney extends Thread {
//        @Override
//        public void run() {
//            while(true) {
//                binding.moneyCount.setText(
//                        Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) + offlineMoney + 1)
//                );
//                try {
//                    sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


//    class OfflineMoney extends Thread {
//        @Override
//        public void run() {
//            for (int i = 0; i < 10; i++) {
//                binding.moneyCount.setText(
//                        Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) + offlineMoney + 1)
//                );
//                try {
//                    sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }



    private void notEnoughMoney() {
        Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
    }

    private void savePref() {
        mySP = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mySP.edit();

//        editor.putInt(String.valueOf(SAVE_MONEY), money);
        editor.putInt(String.valueOf(SAVE_MONEY), Integer.parseInt(binding.moneyCount.getText().toString()));
        editor.putInt(String.valueOf(SAVE_LEVEL_UPGRADE_ADD_MONEY), levelUpgradeAddMoney);
        editor.putInt(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_MONEY), levelUpgradeOfflineMoney);
        editor.putInt(String.valueOf(SAVE_OFFLINE_MONEY), offlineMoney);
        editor.putInt(String.valueOf(SAVE_ADD_MONEY), addMoney);
        editor.putFloat(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_TIME), offlineTime);

        editor.commit();
        Toast.makeText(this, "Progress saved", Toast.LENGTH_SHORT).show();
    }

    private void loadPref() {
        mySP = getPreferences(MODE_PRIVATE);
        float saveFloat = mySP.getFloat(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_TIME), 10f);
        offlineTime = saveFloat;
        int saveInt = mySP.getInt(String.valueOf(SAVE_MONEY), 0);
        binding.moneyCount.setText(Integer.toString(saveInt)); //g



        saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE_ADD_MONEY), 1);
        levelUpgradeAddMoney = saveInt;

        saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_MONEY), 0);
        levelUpgradeOfflineMoney = saveInt;

        saveInt = mySP.getInt(String.valueOf(SAVE_OFFLINE_MONEY), 0);
        offlineMoney = saveInt;

        saveInt = mySP.getInt(String.valueOf(SAVE_ADD_MONEY), 1);
        addMoney = saveInt;


        binding.upgradeAddMoneyPrice.setText("Price: " + Integer.toString(addMoney * 20));
        binding.upgradeOfflineMoneyPrice.setText("Price: " + Integer.toString((offlineMoney + 1) * 200));
        binding.addMoneyCount.setText("Money per\nclick: " + Integer.toString(addMoney));
        binding.addOfflineMoneyCount.setText("Money per\n" + offlineTime + ": " + Integer.toString(offlineMoney));



        Toast.makeText(this, "Progress loaded", Toast.LENGTH_SHORT).show();
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("money", Integer.parseInt(binding.moneyCount.getText().toString()));
        outState.putInt("levelUpgradeAddMoney", levelUpgradeAddMoney);
        outState.putInt("levelUpgradeOfflineMoney", levelUpgradeOfflineMoney);
        outState.putInt("offlineMoney", offlineMoney);
        outState.putInt("addMoney", addMoney);
        outState.putFloat("offlineTime", offlineTime);
    }

    private void loadGetAllPref() {
        if (getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_LEVEL_UPGRADE_ADD_MONEY), 1) != 1) {
            mySP = getPreferences(MODE_PRIVATE);
            int saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE_ADD_MONEY), 1);
            levelUpgradeAddMoney = saveInt;
        }

        if (getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_MONEY), 0) != 0) {
            mySP = getPreferences(MODE_PRIVATE);
            int saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_MONEY), 0);
            levelUpgradeOfflineMoney = saveInt;
        }

        if (getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_OFFLINE_MONEY), 0) != 0) {
            mySP = getPreferences(MODE_PRIVATE);
            int saveInt = mySP.getInt(String.valueOf(SAVE_OFFLINE_MONEY), 0);
            offlineMoney = saveInt;
        }

        if (getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_ADD_MONEY), 1) != 1) {
            mySP = getPreferences(MODE_PRIVATE);
            int saveInt = mySP.getInt(String.valueOf(SAVE_ADD_MONEY), 1);
            addMoney = saveInt;
        }

        if (getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_MONEY), 0) != 0) {
            mySP = getPreferences(MODE_PRIVATE);
            int saveInt = mySP.getInt(String.valueOf(SAVE_MONEY), 0);
            binding.moneyCount.setText(Integer.toString(saveInt));//h
        }

        if (getPreferences(MODE_PRIVATE).getFloat(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_TIME), 10f) != 10f) {
            mySP = getPreferences(MODE_PRIVATE);
            float saveFloat = mySP.getFloat(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_TIME), 10f);
            offlineTime = saveFloat;
        }
    }

    private void onClickBtnUpgradeOfflineMoney() {
        if (Integer.parseInt(binding.moneyCount.getText().toString()) >= (offlineMoney + 1) * 200) {
            binding.moneyCount.setText(
                    Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) - (offlineMoney + 1) * 200)
            );
            levelUpgradeOfflineMoney = levelUpgradeOfflineMoney + 1;
            offlineMoney = offlineMoney + 1;
            binding.addOfflineMoneyCount.setText("Money per\n" + offlineTime + ": " + Integer.toString(offlineMoney));
            binding.upgradeOfflineMoneyPrice.setText("Price: " + Integer.toString((offlineMoney + 1) * 200));
        } else {
            notEnoughMoney();
        }
    }

    private void onClickBtnUpgradeOfflineTime() {
        if (Integer.parseInt(binding.moneyCount.getText().toString()) >= (int) (101 - offlineTime * 10) * 1000) {
            binding.moneyCount.setText(
                    Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) - (int) (101 - offlineTime * 10) * 1000)
            );
            offlineTime = offlineTime - 0.5f;
            binding.addOfflineMoneyCount.setText("Money per\n" + offlineTime + ": " + Integer.toString(offlineMoney));
            binding.upgradeOfflineTimePrice.setText("Price: " + Integer.toString((int) (101 - offlineTime * 10) * 1000));
        } else {
            notEnoughMoney();
        }
    }

    private void onClickBtnUpgradeAddMoney() {
        if (Integer.parseInt(binding.moneyCount.getText().toString()) >= addMoney * 20) {
            binding.moneyCount.setText(
                    Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) - addMoney * 20)
            );
            levelUpgradeAddMoney = levelUpgradeAddMoney + 1;
            addMoney = addMoney + 1;
            binding.addMoneyCount.setText("Money per\nclick: " + Integer.toString(addMoney));
            binding.upgradeAddMoneyPrice.setText("Price: " + Integer.toString(addMoney * 20));
        } else {
            notEnoughMoney();
        }
    }

}