package ru.sunzar.myitschool.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myitschool.R;
import com.example.myitschool.databinding.FragmentClickerBinding;

import java.util.Locale;

import ru.sunzar.myitschool.data.MiningData;
import ru.sunzar.myitschool.data.StocksData;

public class ClickerActivity extends ToolbarBaseFragment {
    private FragmentClickerBinding binding;

    private SharedPreferences sharedMiningData;
    private SharedPreferences sharedStocksData;

    private Handler offlineHandler;
    private Handler afkHandler;

    private long lastClickMills;
    private boolean shownBrokenVideocard = false;
    private boolean onPauseThread = false;
    private int fanCount = 0;

    @Override
    public View onCreateChildView(@NonNull ViewGroup parent, @Nullable Bundle savedInstanceState) {
        binding = FragmentClickerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onPauseThread = false;
        binding.buttonClick.setOnClickListener(view1 -> {
            onClickButtonClick();
        });
        binding.buttonUpgradeAddMoney.setOnClickListener(view2 -> {
            onClickButtonUpgradeAddMoney();
        });
        binding.buttonUpgradeOfflineMoney.setOnClickListener(view3 -> {
            onClickButtonUpgradeOfflineMoney();
        });
        binding.buttonAddOfflineMoneyCount.setOnClickListener(view4 -> {
            onClickButtonAddOfflineMoneyCount();
        });

        binding.buttonBuyVideocard.setOnClickListener(view5 -> {
            onClickButtonBuyVideocard();
        });

        baseBinding.title.setText("Майнинг");

        loadPref();

        lastClickMills = System.currentTimeMillis();
        updateBtcCountMining();
        updateUpgradeAddMoney();
        updateUpgradeAddOfflineMoney();
        updateButtonUpgradeOfflineMoneyCount();
        updatePercentageDurability();
        updateButtonBuyVideocard();
        updateRubCountStocks();

        offlineHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (onPauseThread == false) {
                    if (shownBrokenVideocard == false) {
                        StocksData.setCurrency(StocksData.Currency.ETH, StocksData.getCurrency(StocksData.Currency.ETH).getValue() + MiningData.btc_offline_count);
                        //StocksData.eth_count += MiningData.btc_offline_count;
                        updateBtcCountMining();
                        offlineHandler.sendEmptyMessageDelayed(0, (long) (MiningData.btc_offline_time * 1000));
                    }
                }
            }
        };
        offlineHandler.sendEmptyMessage(0);

        afkHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                long currentClickMills = System.currentTimeMillis();
                if (onPauseThread == false) {
                    if (currentClickMills - lastClickMills > 1000 * 5 && MiningData.percentage_durability_videacard - MiningData.percentage_decrease >= 0) {
                        MiningData.percentage_durability_videacard -= MiningData.percentage_decrease;
                        updatePercentageDurability();
                        shownBrokenVideocard = false;
                    }
                    if (MiningData.percentage_durability_videacard <= 0 && shownBrokenVideocard == false) {
                        binding.percentageDurability.setText("Видеокарта сломалась,\nкупите новую в дополнениях");
                        toastOnBreakingVideocard();
                        shownBrokenVideocard = true;
                    }
                    afkHandler.sendEmptyMessageDelayed(0, (long) (1000));
                }
            }
        };
        afkHandler.sendEmptyMessage(0);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = FragmentClickerBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        binding.stocks.setOnClickListener(view -> {
//            Intent intent = new Intent(this, MainActivity.class);
////            intent.putExtra("index", position);
//            this.startActivity(intent);
//        });
//        binding.buttonClick.setOnClickListener(view -> {
//            onClickButtonClick();
//        });
//        binding.buttonUpgradeAddMoney.setOnClickListener(view -> {
//            onClickButtonUpgradeAddMoney();
//        });
//        binding.buttonUpgradeOfflineMoney.setOnClickListener(view -> {
//            onClickButtonUpgradeOfflineMoney();
//        });
//        binding.buttonAddOfflineMoneyCount.setOnClickListener(view -> {
//            onClickButtonAddOfflineMoneyCount();
//        });
//
//        binding.buttonBuyVideocard.setOnClickListener(view -> {
//            onClickButtonBuyVideocard();
//        });
//
//        loadPref();

        //Loading all savedInstanceState
        ///if (savedInstanceState != null) {
///
        ///    //
        ///    MiningData.btc_count_mining = savedInstanceState.getInt("btc_mining", 0);
        ///    MiningData.btc_add = savedInstanceState.getInt("btc_add", 1);
        ///    MiningData.btc_offline_count = savedInstanceState.getInt("btc_offline_count", 0);
        ///    MiningData.btc_offline_time = savedInstanceState.getFloat("btc_offline_time", 10f);
        ///    //
///
        ///    ///binding.moneyCount.setText(Integer.toString(savedInstanceState.getInt("money")));
        ///    ///binding.upgradeOfflineMoneyPrice.setText("Price: " + Integer.toString((offlineMoney + 1) * 200));
        ///    ///binding.upgradeAddMoneyPrice.setText("Price: " + Integer.toString(addMoney * 20));
///
        ///    ///levelUpgradeAddMoney = savedInstanceState.getInt("levelUpgradeAddMoney");
        ///    ///levelUpgradeOfflineMoney = savedInstanceState.getInt("levelUpgradeOfflineMoney");
        ///    ///offlineMoney = savedInstanceState.getInt("offlineMoney");
        ///    ///addMoney = savedInstanceState.getInt("addMoney");
        ///    ///offlineTime = savedInstanceState.getFloat("offlineTime");
        ///}

        //
//        lastClickMills = System.currentTimeMillis();
//        updateBtcCountMining();
//        updateUpgradeAddMoney();
//        updateUpgradeAddOfflineMoney();
//        updateButtonUpgradeOfflineMoneyCount();
//        updatePercentageDurability();
//        updateButtonBuyVideocard();
//        updateRubCountStocks();
        // ///binding.btcCount.setText(MiningData.btc_count_mining + "");
        // ///binding.priceUpgradeAddMoney.setText("Цена: " + MiningData.btc_add * 20);
        // ///binding.countUpgradeAddMoney.setText("BTC за клик: " + MiningData.btc_add);
        // ///binding.priceUpgradeOfflineMoney.setText("Цена: " + (MiningData.btc_offline_count + 1) * 200);
        // ///binding.countUpgradeOfflineMoney.setText("BTC за " + MiningData.btc_offline_time + " сек: " + MiningData.btc_offline_count);
        // ///binding.priceAddOfflineMoneyCount.setText("Цена: " + (101 - MiningData.btc_offline_time * 10) * 1000);
        // ///binding.timeAddOfflineMoneyCount.setText("Время: " + MiningData.btc_offline_time);
        // ///binding.textUpgradeOfflineMoney.setText("Увеличить BTC\nза " + MiningData.btc_offline_time);
        //

        ///binding.upgradeAddMoneyPrice.setText("Price: " + Integer.toString(addMoney * 20));
        ///binding.upgradeOfflineMoneyPrice.setText("Price: " + Integer.toString((offlineMoney + 1) * 200));
        ///binding.addMoneyCount.setText("Money per\nclick: " + Integer.toString(addMoney));
        ///binding.addOfflineMoneyCount.setText("Money per\n" + offlineTime + ": " + Integer.toString(offlineMoney));

        //Checking all buttons for click
        ///binding.buttonMoney.setOnClickListener(new View.OnClickListener() {
        ///    @Override
        ///    public void onClick(View view) {
        ///        moneyStr = binding.moneyCount.getText().toString();
        ///        money = Integer.parseInt(moneyStr) + addMoney;
        ///        moneyStr = Integer.toString(money);
        ///        binding.moneyCount.setText(moneyStr);
        ///    }
        ///});
///
        ///binding.buttonUpgradeAddMoney.setOnClickListener(new View.OnClickListener() {
        ///    @Override
        ///    public void onClick(View view) {
        ///        onClickBtnUpgradeAddMoney();
        ///    }
        ///});
///
        ///binding.buttonUpgradeOfflineMoney.setOnClickListener(new View.OnClickListener() {
        ///    @Override
        ///    public void onClick(View view) {
        ///        onClickBtnUpgradeOfflineMoney();
        ///    }
        ///});
///
        ///binding.buttonSave.setOnClickListener(new View.OnClickListener() {
        ///    @Override
        ///    public void onClick(View view) {
        ///        savePref();
        ///    }
        ///});
///
        ///binding.buttonLoad.setOnClickListener(new View.OnClickListener() {
        ///    @Override
        ///    public void onClick(View view) {
        ///        loadPref();
        ///    }
        ///});
///
        ///binding.buttonUpgradeOfflineTime.setOnClickListener(new View.OnClickListener() {
        ///    @Override
        ///    public void onClick(View view) {
        ///        onClickBtnUpgradeOfflineTime();
        ///    }
        ///});

//        offlineHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                StocksData.eth_count += MiningData.btc_offline_count;
//                updateBtcCountMining();
//                offlineHandler.sendEmptyMessageDelayed(0, (long) (MiningData.btc_offline_time * 1000));
//            }
//        };
//        offlineHandler.sendEmptyMessage(0);
//
//        afkHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                long currentClickMills = System.currentTimeMillis();
//                if (currentClickMills - lastClickMills > 1000 * 5 && MiningData.percentage_durability_videacard - MiningData.percentage_decrease >= 0) {
//                    MiningData.percentage_durability_videacard -= MiningData.percentage_decrease;
//                    updatePercentageDurability();
//                    shownBrokenVideocard = false;
//                }
//                if (MiningData.percentage_durability_videacard <= 0 && shownBrokenVideocard == false) {
//                    binding.percentageDurability.setText("Видеокарта сломалась,\nкупите новую в магазине");
//                    toastOnBreakingVideocard();
//                    shownBrokenVideocard = true;
//                }
//                afkHandler.sendEmptyMessageDelayed(0, (long) (1000));
//            }
//        };
//        afkHandler.sendEmptyMessage(0);
//    }

    private void onClickButtonClick() {
        if (MiningData.percentage_durability_videacard > 0) {
            StocksData.setCurrency(StocksData.Currency.ETH, StocksData.getCurrency(StocksData.Currency.ETH).getValue() + (float) Math.floor(StocksData.getCurrency(StocksData.Currency.ETH).getValue() + MiningData.btc_add * 1000000) / 1000000);
            //StocksData.eth_count = StocksData.eth_count + (float) Math.floor(StocksData.eth_count + MiningData.btc_add * 1000) / 1000;
            updateBtcCountMining();
            long currentClickMills = System.currentTimeMillis();
            if (currentClickMills - lastClickMills > 1000)
                lastClickMills = System.currentTimeMillis();
//            if (fanCount < 4) {
//                fanCount += 1;
//                switch (fanCount) {
//                    case 0:
//                        binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card36));
//                        break;
//                    case 1:
//                        binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card27));
//                        break;
//                    case 2:
//                        binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card18));
//                        break;
//                    case 3:
//                        binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card9));
//                        break;
//                    case 4:
//                        binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card1));
//                        break;
//                }
//            } else {
//                binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card36));
//                fanCount = 0;
//            }
            if (fanCount < 2) {
                fanCount += 1;
                switch (fanCount) {
                    case 0:
                        binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card30));
                        break;
                    case 1:
                        binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card15));
                        break;
                    case 2:
                        binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card1));
                        break;
                }
            } else {
                binding.buttonClick.setBackground(getResources().getDrawable(R.drawable.graphics_card30));
                fanCount = 0;
            }
        }
    }

    private void onClickButtonUpgradeAddMoney() {
        if (StocksData.getCurrency(StocksData.Currency.ETH).getValue() >= MiningData.btc_add * 100) {
            StocksData.setCurrency(StocksData.Currency.ETH, StocksData.getCurrency(StocksData.Currency.ETH).getValue() - MiningData.btc_add * 100);
            MiningData.btc_add += 0.000005f;
            MiningData.btc_add = (float) Math.ceil(MiningData.btc_add * 10000) / 10000;
            updateUpgradeAddMoney();
            updateBtcCountMining();
            Toast.makeText(this.getActivity(), "Улучшение куплено!", Toast.LENGTH_SHORT).show();
        } else {
            notEnoughMoney();
        }
    }

    private void onClickButtonUpgradeOfflineMoney() {
        if (StocksData.getCurrency(StocksData.Currency.ETH).getValue() >= (MiningData.btc_offline_count + 0.0001f) * 200) {
            StocksData.setCurrency(StocksData.Currency.ETH, StocksData.getCurrency(StocksData.Currency.ETH).getValue() - (MiningData.btc_offline_count + 0.0001f) * 200);
            MiningData.btc_offline_count += 0.0001f;
            updateUpgradeAddOfflineMoney();
            updateBtcCountMining();
            Toast.makeText(this.getActivity(), "Улучшение куплено!", Toast.LENGTH_SHORT).show();
        } else {
            notEnoughMoney();
        }
    }

    private void onClickButtonAddOfflineMoneyCount() {
        if (StocksData.getCurrency(StocksData.Currency.ETH).getValue() >= (101 - MiningData.btc_offline_time * 10) * 0.05f && MiningData.btc_offline_time >= 5.5f) {
            StocksData.setCurrency(StocksData.Currency.ETH, StocksData.getCurrency(StocksData.Currency.ETH).getValue() - (101 - MiningData.btc_offline_time * 10) * 0.05f);
            MiningData.btc_offline_time -= 0.5f;
            updateButtonUpgradeOfflineMoneyCount();
            updateBtcCountMining();
            Toast.makeText(this.getActivity(), "Улучшение куплено!", Toast.LENGTH_SHORT).show();
        } else {
            notEnoughMoney();
        }
    }

    private void onClickButtonBuyVideocard() {
        if (StocksData.getCurrency(StocksData.Currency.ETH).getValue() >= MiningData.price_buy_videocard) {
            StocksData.setCurrency(StocksData.Currency.ETH, StocksData.getCurrency(StocksData.Currency.ETH).getValue() - MiningData.price_buy_videocard);
            MiningData.percentage_durability_videacard = 100;
            lastClickMills = System.currentTimeMillis();
            updateButtonBuyVideocard();
            updatePercentageDurability();
            updateBtcCountMining();
            Toast.makeText(this.getActivity(), "Видеокарта куплена! Вы можете продолжать майнить", Toast.LENGTH_SHORT).show();
        } else {
            notEnoughMoney();
        }
    }

    private void notEnoughMoney() {
        Toast.makeText(this.getActivity(), "Недостаточно BTC", Toast.LENGTH_SHORT).show();
    }

    private void toastOnBreakingVideocard() {
        Toast.makeText(this.getActivity(), "Видеокарта перегрелась и сгорела, купите новую в магазине", Toast.LENGTH_LONG).show();
    }

    private void updateBtcCountMining() {
        //binding.btcCount.setText("BTC: " + StocksData.getCurrency(StocksData.Currency.ETH).getValue());
        MiningData.price_buy_videocard = (float) Math.floor(StocksData.getCurrency(StocksData.Currency.ETH).getValue() * 0.5f * 1000000) / 1000000;
        updateButtonBuyVideocard();
    }

    private void updateUpgradeAddMoney() {
        binding.priceUpgradeAddMoney.setText(String.format(
                Locale.getDefault(),
                "Цена: %.6f",
                (float) Math.ceil(MiningData.btc_add * 100 * 10000) / 10000)
        );
        binding.countUpgradeAddMoney.setText(String.format(
                Locale.getDefault(),
                " %.6f",
                MiningData.btc_add
        ));
    }

    private void updateUpgradeAddOfflineMoney() {
        binding.priceUpgradeOfflineMoney.setText(String.format(
                Locale.getDefault(),
                "Цена: %.6f",
                (MiningData.btc_offline_count + 0.0001f) * 200
        ));
        binding.countUpgradeOfflineMoney.setText(String.format(
                Locale.getDefault(),
                " %.6f",
                MiningData.btc_offline_count
        ));
    }

    private void updateButtonUpgradeOfflineMoneyCount() {
        binding.priceAddOfflineMoneyCount.setText(String.format(
                Locale.getDefault(),
                "Цена: %.6f",
                (101 - MiningData.btc_offline_time * 10) * 0.05f
        ));
        binding.timeAddOfflineMoneyCount.setText(String.format(
                Locale.getDefault(),
                "%.1f сек",
                MiningData.btc_offline_time
        ));
        binding.textUpgradeOfflineMoney.setText(String.format(
                Locale.getDefault(),
                "Количество за %.1f сек",
                MiningData.btc_offline_time
        ));
        binding.countUpgradeOfflineMoney.setText(String.format(
                Locale.getDefault(),
                " %.6f",
                MiningData.btc_offline_count
        ));
    }

    private void updatePercentageDurability() {
        binding.percentageDurability.setText("Прочность видеокарты: " + MiningData.percentage_durability_videacard + "%");
    }

    private void updateButtonBuyVideocard() {
        binding.priceBuyVideocard.setText(String.format(
                Locale.getDefault(),
                "Цена: %.6f",
                MiningData.price_buy_videocard
        ));
    }

    private void updateRubCountStocks() {
        //TODO: binding.rubCount.setText("RUB: " + StocksData.rub_count);
    }




    private void savePref() {
//        mySP = this.getPreferences(MODE_PRIVATE);
//        mySP = getSharedPreferences("jlj", MODE_PRIVATE);
        sharedMiningData = this.getActivity().getSharedPreferences("mining_data", MODE_PRIVATE);
        //sharedStocksData = this.getActivity().getSharedPreferences("stocks_data", MODE_PRIVATE);
        SharedPreferences.Editor editorMiningData = sharedMiningData.edit();
        //SharedPreferences.Editor editorStocksData = sharedStocksData.edit();

        //editorStocksData.putFloat("eth_count_pref", StocksData.eth_count);
        editorMiningData.putFloat("btc_add_pref", MiningData.btc_add);
        editorMiningData.putFloat("btc_offline_count_pref", MiningData.btc_offline_count);
        editorMiningData.putFloat("btc_offline_time_pref", MiningData.btc_offline_time);
        editorMiningData.putInt("percentage_durability_videacard_pref", MiningData.percentage_durability_videacard);
        editorMiningData.putInt("percentage_decrease_pref", MiningData.percentage_decrease);

        editorMiningData.apply();
        //editorStocksData.apply();
        //Toast.makeText(this.getActivity(), "Данные сохранены", Toast.LENGTH_SHORT).show();
    }

    private void loadPref() {
//        mySP = this.getPreferences(MODE_PRIVATE);
        sharedMiningData = this.getActivity().getSharedPreferences("mining_data", MODE_PRIVATE);
        //sharedStocksData = this.getActivity().getSharedPreferences("stocks_data", MODE_PRIVATE);
        //
        //StocksData.eth_count = sharedStocksData.getFloat("eth_count_pref", 0);
        //StocksData.setRub(sharedStocksData.getFloat("rub_count_pref", 0));
        MiningData.btc_add = sharedMiningData.getFloat("btc_add_pref", 0.000005f);
        MiningData.btc_offline_count = sharedMiningData.getFloat("btc_offline_count_pref", 0.0f);
        MiningData.btc_offline_time = sharedMiningData.getFloat("btc_offline_time_pref", 10f);
        MiningData.percentage_durability_videacard = sharedMiningData.getInt("percentage_durability_videacard_pref", 100);
        MiningData.percentage_decrease = sharedMiningData.getInt("percentage_decrease_pref", 10);
        //

        ///float saveFloat = mySP.getFloat(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_TIME), 10f);
        ///offlineTime = saveFloat;
        ///int saveInt = mySP.getInt(String.valueOf(SAVE_MONEY), 0);
        ///binding.moneyCount.setText(Integer.toString(saveInt)); //g
///
///
///
        ///saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE_ADD_MONEY), 1);
        ///levelUpgradeAddMoney = saveInt;
///
        ///saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_MONEY), 0);
        ///levelUpgradeOfflineMoney = saveInt;
///
        ///saveInt = mySP.getInt(String.valueOf(SAVE_OFFLINE_MONEY), 0);
        ///offlineMoney = saveInt;
///
        ///saveInt = mySP.getInt(String.valueOf(SAVE_ADD_MONEY), 1);
        ///addMoney = saveInt;
///
///
        ///binding.upgradeAddMoneyPrice.setText("Price: " + Integer.toString(addMoney * 20));
        ///binding.upgradeOfflineMoneyPrice.setText("Price: " + Integer.toString((offlineMoney + 1) * 200));
        ///binding.addMoneyCount.setText("Money per\nclick: " + Integer.toString(addMoney));
        ///binding.addOfflineMoneyCount.setText("Money per\n" + offlineTime + ": " + Integer.toString(offlineMoney));
///
///
///
        ///Toast.makeText(this.getActivity(), "Progress loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        lastClickMills = System.currentTimeMillis();
        onPauseThread = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        onPauseThread = true;
        savePref();
    }

    //@Override
    //protected void onPause() {
    //    super.onPause();
    //    savePref();
    //}
//
    //@Override
    //protected void onResume() {
    //    super.onResume();
    //    lastClickMills = System.currentTimeMillis();
    //}

    //@Override
    //public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
    //    super.onSaveInstanceState(outState, outPersistentState);
//
    //    outState.putFloat("btc_count", StocksData.eth_count);
    //    outState.putInt("btc_add", MiningData.btc_add);
    //    outState.putInt("btc_offline_count", MiningData.btc_offline_count);
    //    outState.putFloat("btc_offline_time", MiningData.btc_offline_time);
    //}
//
    //@Override
    //protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    //    super.onRestoreInstanceState(savedInstanceState);
//
    //    StocksData.eth_count = savedInstanceState.getFloat("btc_count", 0);
    //    MiningData.btc_add = savedInstanceState.getInt("btc_add", 1);
    //    MiningData.btc_offline_count = savedInstanceState.getInt("btc_offline_count", 0);
    //    MiningData.btc_offline_time = savedInstanceState.getFloat("btc_offline_time", 10f);
//
    //    updateBtcCountMining();
    //    updateUpgradeAddMoney();
    //    updateUpgradeAddOfflineMoney();
    //    updateButtonUpgradeOfflineMoneyCount();
    //}

    ///public void onSaveInstanceState(@NonNull Bundle outState) {
    ///    super.onSaveInstanceState(outState);
    ///    //
    ///    outState.putInt("btc_mining", MiningData.btc_count_mining);
    ///    outState.putInt("btc_add", MiningData.btc_add);
    ///    outState.putInt("btc_offline_count", MiningData.btc_offline_count);
    ///    outState.putDouble("btc_offline_time", MiningData.btc_offline_time);
    ///    //
    ///    ///outState.putInt("money", Integer.parseInt(binding.moneyCount.getText().toString()));
    ///    ///outState.putInt("levelUpgradeAddMoney", levelUpgradeAddMoney);
    ///    ///outState.putInt("levelUpgradeOfflineMoney", levelUpgradeOfflineMoney);
    ///    ///outState.putInt("offlineMoney", offlineMoney);
    ///    ///outState.putInt("addMoney", addMoney);
    ///    ///outState.putFloat("offlineTime", offlineTime);
    ///}

    ////private void loadGetAllPref() {
    ////    if (this.getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_LEVEL_UPGRADE_ADD_MONEY), 1) != 1) {
    ////        mySP = this.getPreferences(MODE_PRIVATE);
    ////        int saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE_ADD_MONEY), 1);
    ////        levelUpgradeAddMoney = saveInt;
    ////    }
////
    ////    if (this.getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_MONEY), 0) != 0) {
    ////        mySP = this.getPreferences(MODE_PRIVATE);
    ////        int saveInt = mySP.getInt(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_MONEY), 0);
    ////        levelUpgradeOfflineMoney = saveInt;
    ////    }
////
    ////    if (this.getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_OFFLINE_MONEY), 0) != 0) {
    ////        mySP = this.getPreferences(MODE_PRIVATE);
    ////        int saveInt = mySP.getInt(String.valueOf(SAVE_OFFLINE_MONEY), 0);
    ////        offlineMoney = saveInt;
    ////    }
////
    ////    if (this.getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_ADD_MONEY), 1) != 1) {
    ////        mySP = this.getPreferences(MODE_PRIVATE);
    ////        int saveInt = mySP.getInt(String.valueOf(SAVE_ADD_MONEY), 1);
    ////        addMoney = saveInt;
    ////    }
////
    ////    if (this.getPreferences(MODE_PRIVATE).getInt(String.valueOf(SAVE_MONEY), 0) != 0) {
    ////        mySP = this.getPreferences(MODE_PRIVATE);
    ////        int saveInt = mySP.getInt(String.valueOf(SAVE_MONEY), 0);
    ////        binding.moneyCount.setText(Integer.toString(saveInt));//h
    ////    }
////
    ////    if (this.getPreferences(MODE_PRIVATE).getFloat(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_TIME), 10f) != 10f) {
    ////        mySP = this.getPreferences(MODE_PRIVATE);
    ////        float saveFloat = mySP.getFloat(String.valueOf(SAVE_LEVEL_UPGRADE_OFFLINE_TIME), 10f);
    ////        offlineTime = saveFloat;
    ////    }
    ////}

//    private void onClickBtnUpgradeOfflineMoney() {
//        if (Integer.parseInt(binding.moneyCount.getText().toString()) >= (offlineMoney + 1) * 200) {
//            binding.moneyCount.setText(
//                    Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) - (offlineMoney + 1) * 200)
//            );
//            levelUpgradeOfflineMoney = levelUpgradeOfflineMoney + 1;
//            offlineMoney = offlineMoney + 1;
//            binding.addOfflineMoneyCount.setText("Money per\n" + offlineTime + ": " + Integer.toString(offlineMoney));
//            binding.upgradeOfflineMoneyPrice.setText("Price: " + Integer.toString((offlineMoney + 1) * 200));
//        } else {
//            notEnoughMoney();
//        }
//    }
//
//    private void onClickBtnUpgradeOfflineTime() {
//        if (Integer.parseInt(binding.moneyCount.getText().toString()) >= (int) (101 - offlineTime * 10) * 1000) {
//            binding.moneyCount.setText(
//                    Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) - (int) (101 - offlineTime * 10) * 1000)
//            );
//            offlineTime = offlineTime - 0.5f;
//            binding.addOfflineMoneyCount.setText("Money per\n" + offlineTime + ": " + Integer.toString(offlineMoney));
//            binding.upgradeOfflineTimePrice.setText("Price: " + Integer.toString((int) (101 - offlineTime * 10) * 1000));
//        } else {
//            notEnoughMoney();
//        }
//    }
//
//    private void onClickBtnUpgradeAddMoney() {
//        if (Integer.parseInt(binding.moneyCount.getText().toString()) >= addMoney * 20) {
//            binding.moneyCount.setText(
//                    Integer.toString(Integer.parseInt(binding.moneyCount.getText().toString()) - addMoney * 20)
//            );
//            levelUpgradeAddMoney = levelUpgradeAddMoney + 1;
//            addMoney = addMoney + 1;
//            binding.addMoneyCount.setText("Money per\nclick: " + Integer.toString(addMoney));
//            binding.upgradeAddMoneyPrice.setText("Price: " + Integer.toString(addMoney * 20));
//        } else {
//            notEnoughMoney();
//        }
//    }

}
