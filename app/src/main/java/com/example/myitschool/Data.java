package com.example.myitschool;

import android.content.SharedPreferences;
import android.os.Handler;

public class Data {

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

    public void savePrefsData() {

    }
}
