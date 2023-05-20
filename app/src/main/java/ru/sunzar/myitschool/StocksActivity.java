package ru.sunzar.myitschool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.myitschool.R;
import com.example.myitschool.databinding.FragmentStocksBinding;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.sunzar.myitschool.data.StockGraphData;
import ru.sunzar.myitschool.data.StocksData;
import ru.sunzar.myitschool.data.StocksGraphRepository;
import ru.sunzar.myitschool.data.StocksRepository;
import ru.sunzar.myitschool.utils.Resource;

public class StocksActivity extends AppCompatActivity {
    private FragmentStocksBinding binding;

    private final StocksRepository repository = new StocksRepository();
    private final StocksGraphRepository graphRepository = new StocksGraphRepository();

    private StocksRepositoryData shortRepository = new StocksRepositoryData();

    private final StocksAdapter adapter = new StocksAdapter();

    private StocksData.Currency currency;
    private XYPlot plot;

    private float currencyPrice;
    private float currencyCount;

    private boolean buyMode = true;
    private boolean textCurrencyOnChange = false;
    private boolean textRubOnChange = false;
    private boolean textOnClickPercentages = false;
    private boolean onClickPercentages25 = false;
    private boolean onClickPercentages50 = false;
    private boolean onClickPercentages75 = false;
    private boolean onClickPercentages100 = false;

    public static Intent newIntent(Context context, StocksData.Currency currency) {
        Intent intent = new Intent(context, StocksActivity.class);
        intent.putExtra("currency", currency);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentStocksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currency = (StocksData.Currency) getIntent().getSerializableExtra("currency");
        long longDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(longDate);
        repository.search(sDate);
        plot = (XYPlot) binding.plot;
        goSearchStocks();

        graphRepository.stockGraphDataLiveData.observe(this, stocksSearchResponseResource -> {
            Log.d("LOG", "RESPONSE SUCCESS: " + stocksSearchResponseResource.toString());
            if (stocksSearchResponseResource instanceof Resource.Success) {
                onUpdateGraphData((Resource.Success<StockGraphData>) stocksSearchResponseResource);
            } else if (stocksSearchResponseResource instanceof Resource.Error) {
                showError();
            } else if (stocksSearchResponseResource instanceof Resource.Loading) {
                showLoading(); //Не забудь скрыть загрузку в других состояниях
            }
        });

        loadStocksData();

        binding.buy.setOnClickListener(view -> {
            onClickButtonBuy();
        });

        binding.sell.setOnClickListener(view -> {
            onClickButtonSell();
        });

        binding.buttonAction.setOnClickListener(view -> {
            onClickButtonAction();
        });

        binding.currencyCountAction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textCurrencyOnChange = true;
                if (textRubOnChange == false) {
                    String currencyCountString = binding.currencyCountAction.getText().toString();
                    if (currencyCountString.trim().length() != 0 && currencyCountString != "") {
                        float currencyCount = Float.parseFloat(currencyCountString);
                        binding.rubCountAction.setText(currencyCount * currencyPrice + "");
                    } else {
                        binding.rubCountAction.setText("");
                    }
                    if (textOnClickPercentages != true) {
                        resetPercentagesOfBalance();
                        resetPercentagesOfBalanceBooleans();
                    }
                }
                textCurrencyOnChange = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.rubCountAction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textRubOnChange = true;
                if (textCurrencyOnChange == false) {
                    String rubCountString = binding.rubCountAction.getText().toString();
                    if (rubCountString.trim().length() != 0 && rubCountString != "") {
                        float rubCount = Float.parseFloat(rubCountString);
                        if (buyMode != true) {
                            binding.currencyCountAction.setText(Math.floor(rubCount / currencyPrice * 100) / 100 + "");
                        } else {
                            binding.currencyCountAction.setText(Math.floor(rubCount / currencyPrice * 100) / 100 + "");
                        }
                    } else {
                        binding.currencyCountAction.setText("");
                    }
                    if (textOnClickPercentages != true) {
                        resetPercentagesOfBalance();
                        resetPercentagesOfBalanceBooleans();
                    }
                }
                textRubOnChange = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.percentagesOfBalance25.setOnClickListener(view -> {
            onClickPercentagesOfBalance25();
        });

        binding.percentagesOfBalance50.setOnClickListener(view -> {
            onClickPercentagesOfBalance50();
        });

        binding.percentagesOfBalance75.setOnClickListener(view -> {
            onClickPercentagesOfBalance75();
        });

        binding.percentagesOfBalance100.setOnClickListener(view -> {
            onClickPercentagesOfBalance100();
        });
    }

    private void resetPercentagesOfBalance() {
        binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
    }

    private void resetPercentagesOfBalanceBooleans() {
        onClickPercentages25 = false;
        onClickPercentages50 = false;
        onClickPercentages75 = false;
        onClickPercentages100 = false;
    }

    private void onClickPercentagesOfBalance25() {
        textCurrencyOnChange = true;
        if (onClickPercentages25 != true) {
            if (buyMode != true) {
                binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.textShadow));
                binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.textShadow));
                binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
                textOnClickPercentages = true;
                textRubOnChange = false;
                binding.currencyCountAction.setText(Math.floor(currencyCount * 0.25 * 100) / 100 + "");
                textOnClickPercentages = false;
            } else {
                binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.textShadow));
                binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.textShadow));
                binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
                textOnClickPercentages = true;
                textRubOnChange = false;
                binding.currencyCountAction.setText(Math.floor(StocksData.getCurrency(StocksData.Currency.RUB).getValue() / currencyPrice * 0.25 * 100) / 100 + "");
                textOnClickPercentages = false;
            }
            resetPercentagesOfBalanceBooleans();
            onClickPercentages25 = true;
        } else {
            resetPercentagesOfBalance();
            textOnClickPercentages = true;
            textRubOnChange = false;
            binding.currencyCountAction.setText("");
            textOnClickPercentages = false;
            onClickPercentages25 = false;
        }
    }

    private void onClickPercentagesOfBalance50() {
        textCurrencyOnChange = true;
        if (onClickPercentages50 != true) {
            if (buyMode != true) {
                binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.textShadow));
                binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
                textOnClickPercentages = true;
                textRubOnChange = false;
                binding.currencyCountAction.setText(Math.floor(currencyCount * 0.5 * 100) / 100 + "");
                textOnClickPercentages = false;
            } else {
                binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.textShadow));
                binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
                textOnClickPercentages = true;
                textRubOnChange = false;
                binding.currencyCountAction.setText(Math.floor(StocksData.getCurrency(StocksData.Currency.RUB).getValue() / currencyPrice * 0.5 * 100) / 100 + "");
                textOnClickPercentages = false;
            }
            resetPercentagesOfBalanceBooleans();
            onClickPercentages50 = true;
        } else {
            resetPercentagesOfBalance();
            textOnClickPercentages = true;
            textRubOnChange = false;
            binding.currencyCountAction.setText("");
            textOnClickPercentages = false;
            onClickPercentages50 = false;
        }
    }

    private void
    onClickPercentagesOfBalance75() {
        textCurrencyOnChange = true;
        if (onClickPercentages75 != true) {
            if (buyMode != true) {
                binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
                textOnClickPercentages = true;
                textRubOnChange = false;
                binding.currencyCountAction.setText(Math.floor(currencyCount * 0.75 * 100) / 100 + "");
                textOnClickPercentages = false;
            } else {
                binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
                binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
                textOnClickPercentages = true;
                textRubOnChange = false;
                binding.currencyCountAction.setText(Math.floor(StocksData.getCurrency(StocksData.Currency.RUB).getValue() / currencyPrice * 0.75 * 100) / 100 + "");
                textOnClickPercentages = false;
            }
            resetPercentagesOfBalanceBooleans();
            onClickPercentages75 = true;
        } else {
            resetPercentagesOfBalance();
            textOnClickPercentages = true;
            textRubOnChange = false;
            binding.currencyCountAction.setText("");
            textOnClickPercentages = false;
            onClickPercentages75 = false;
        }
    }

    private void onClickPercentagesOfBalance100() {
        textCurrencyOnChange = true;
        textRubOnChange = true;
        if (onClickPercentages100 != true) {
            if (buyMode != true) {
                binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
                binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.white));
                textOnClickPercentages = true;
                textRubOnChange = false;
                binding.currencyCountAction.setText(currencyCount + "");
                textOnClickPercentages = false;
            } else {
                binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
                binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.white));
                binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.white));
                textOnClickPercentages = true;
                textCurrencyOnChange = false;
                binding.rubCountAction.setText(StocksData.getCurrency(StocksData.Currency.RUB).getValue() + "");
                textOnClickPercentages = false;
            }
            resetPercentagesOfBalanceBooleans();
            onClickPercentages100 = true;
        } else {
            resetPercentagesOfBalance();
            textOnClickPercentages = true;
            textRubOnChange = false;
            binding.currencyCountAction.setText("");
            textOnClickPercentages = false;
            onClickPercentages100 = false;
        }
    }

    private void onClickButtonBuy() {
        String currencyName = currency.getApiName().toUpperCase();
        buyMode = true;
        resetPercentagesOfBalanceBooleans();
        if (buyMode != true) {
            binding.buy.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
            binding.sell.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
            binding.buttonAction.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
            binding.buy.setTextColor(getResources().getColorStateList(R.color.textNotChoose));
            binding.sell.setTextColor(getResources().getColorStateList(R.color.white));
            binding.buttonAction.setText("Продать " + currencyName);
        } else {
            binding.buy.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
            binding.sell.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
            binding.buttonAction.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
            binding.sell.setTextColor(getResources().getColorStateList(R.color.textNotChoose));
            binding.buy.setTextColor(getResources().getColorStateList(R.color.white));
            binding.buttonAction.setText("Купить " + currencyName);
        }
        binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.currencyCountAction.setText("");
    }

    private void onClickButtonSell() {
        String currencyName = currency.getApiName().toUpperCase();
        buyMode = false;
        resetPercentagesOfBalanceBooleans();
        if (buyMode != true) {
            binding.buy.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
            binding.sell.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
            binding.buttonAction.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
            binding.buy.setTextColor(getResources().getColorStateList(R.color.textNotChoose));
            binding.sell.setTextColor(getResources().getColorStateList(R.color.white));
            binding.buttonAction.setText("Продать " + currencyName);
        } else {
            binding.buy.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
            binding.sell.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
            binding.buttonAction.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
            binding.sell.setTextColor(getResources().getColorStateList(R.color.textNotChoose));
            binding.buy.setTextColor(getResources().getColorStateList(R.color.white));
            binding.buttonAction.setText("Купить " + currencyName);
        }
        binding.percentagesOfBalance25.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance50.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance75.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance100.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
        binding.percentagesOfBalance25.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance50.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance75.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.percentagesOfBalance100.setTextColor(getResources().getColorStateList(R.color.textShadow));
        binding.currencyCountAction.setText("");
    }

    private void onClickButtonAction() {
        /*
        String currencyCountString = binding.currencyCountAction.getText().toString();
        if (buyMode != true) {
            if (currencyCountString.trim().length() != 0 && currencyCountString != "") {
                float currencyCount = Float.parseFloat(currencyCountString);
                switch (currencyId) {
                    case "btc":
                        if (StocksData.btc_count >= currencyCount) {
                            StocksData.btc_count -= currencyCount;
                            StocksData.rub_count += currencyCount * currencyPrice;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.btc_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "eur":
                        if (StocksData.eur_count >= currencyCount) {
                            StocksData.eur_count -= currencyCount;
                            StocksData.rub_count += currencyCount * currencyPrice;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.eur_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "matic":
                        if (StocksData.matic_count >= currencyCount) {
                            StocksData.matic_count -= currencyCount;
                            StocksData.rub_count += currencyCount * currencyPrice;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.matic_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "doge":
                        if (StocksData.doge_count >= currencyCount) {
                            StocksData.doge_count -= currencyCount;
                            StocksData.rub_count += currencyCount * currencyPrice;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.doge_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "cny":
                        if (StocksData.cny_count >= currencyCount) {
                            StocksData.cny_count -= currencyCount;
                            StocksData.rub_count += currencyCount * currencyPrice;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.cny_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "eth":
                        if (StocksData.eth_count >= currencyCount) {
                            StocksData.eth_count -= currencyCount;
                            StocksData.rub_count += currencyCount * currencyPrice;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.eth_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "1inch":
                        if (StocksData.inch1_count >= currencyCount) {
                            StocksData.inch1_count -= currencyCount;
                            StocksData.rub_count += currencyCount * currencyPrice;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.inch1_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "ltc":
                        if (StocksData.ltc_count >= currencyCount) {
                            StocksData.ltc_count -= currencyCount;
                            StocksData.rub_count += currencyCount * currencyPrice;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.ltc_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                }
            } else {
                enterNumber();
            }
        }

        else {
            if (currencyCountString.trim().length() != 0 && currencyCountString != "") {
                float currencyCount = Float.parseFloat(currencyCountString);
                switch (currencyId) {
                    case "btc":
                        if (StocksData.rub_count >= currencyCount * currencyPrice) {
                            StocksData.rub_count -= currencyCount * currencyPrice;
                            StocksData.btc_count += currencyCount;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.btc_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "eur":
                        if (StocksData.rub_count >= currencyCount * currencyPrice) {
                            StocksData.rub_count -= currencyCount * currencyPrice;
                            StocksData.eur_count += currencyCount;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.eur_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "matic":
                        if (StocksData.rub_count >= currencyCount * currencyPrice) {
                            StocksData.rub_count -= currencyCount * currencyPrice;
                            StocksData.matic_count += currencyCount;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.matic_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "doge":
                        if (StocksData.rub_count >= currencyCount * currencyPrice) {
                            StocksData.rub_count -= currencyCount * currencyPrice;
                            StocksData.doge_count += currencyCount;
                            binding.currencyCount.setText(currencyId.toUpperCase() + StocksData.doge_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "cny":
                        if (StocksData.rub_count >= currencyCount * currencyPrice) {
                            StocksData.rub_count -= currencyCount * currencyPrice;
                            StocksData.cny_count += currencyCount;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.cny_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "eth":
                        if (StocksData.rub_count >= currencyCount * currencyPrice) {
                            StocksData.rub_count -= currencyCount * currencyPrice;
                            StocksData.eth_count += currencyCount;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.eth_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "1inch":
                        if (StocksData.rub_count >= currencyCount * currencyPrice) {
                            StocksData.rub_count -= currencyCount * currencyPrice;
                            StocksData.inch1_count += currencyCount;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.inch1_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                    case "ltc":
                        if (StocksData.rub_count >= currencyCount * currencyPrice) {
                            StocksData.rub_count -= currencyCount * currencyPrice;
                            StocksData.ltc_count += currencyCount;
                            binding.currencyCount.setText(currencyId.toUpperCase() + ": " + StocksData.ltc_count + "");
                            binding.rubCount.setText("RUB: " + StocksData.rub_count + "");
                        } else {
                            notEnoughMoney();
                        }
                        break;
                }
            } else {
                enterNumber();
            }
        }*/
    }

    private void enterNumber() {
        Toast.makeText(this, "Введите число", Toast.LENGTH_SHORT).show();
    }

    private void notEnoughMoney() {
        Toast.makeText(this, "Недостаточно средств", Toast.LENGTH_SHORT).show();
    }

    private void loadStocksData() {
        String currencyName = currency.getApiName().toUpperCase();
        StocksData.getCurrency(currency).observe(this, value -> {
            binding.currencyCount.setText(
                    String.format(
                            Locale.getDefault(),
                            "%s: %.4f",
                            currencyName,
                            value
                    )
            );
        });
        StocksData.getCurrency(StocksData.Currency.RUB).observe(this, value -> {
            binding.rubCount.setText(
                    String.format(
                            Locale.getDefault(),
                            "%s: %.4f",
                            StocksData.Currency.RUB.getApiName().toUpperCase(),
                            value
                    )
            );
        });
        binding.name.setText(currencyName + "/RUB");
        binding.buy.setText("Купить " + currencyName);
        binding.sell.setText("Продать " + currencyName);
        binding.currencyCountAction.setHint("Количество (" + currencyName + ")");
        binding.buttonAction.setText("Продать/Купить " + currencyName);
        if (buyMode != true) {
            binding.buy.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
            binding.sell.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
            binding.buttonAction.setBackgroundTintList(getResources().getColorStateList(R.color.sell));
            binding.buy.setTextColor(getResources().getColorStateList(R.color.textNotChoose));
            binding.sell.setTextColor(getResources().getColorStateList(R.color.white));
            binding.buttonAction.setText("Продать " + currencyName);
        } else {
            binding.buy.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
            binding.sell.setBackgroundTintList(getResources().getColorStateList(R.color.shadow));
            binding.buttonAction.setBackgroundTintList(getResources().getColorStateList(R.color.buy));
            binding.sell.setTextColor(getResources().getColorStateList(R.color.textNotChoose));
            binding.buy.setTextColor(getResources().getColorStateList(R.color.white));
            binding.buttonAction.setText("Купить " + currencyName);
        }
    }

    private void showLoading() {

    }

    private void showError() {

    }

    private void onUpdateGraphData(Resource.Success<StockGraphData> state) {
        XYSeries series1 = new SimpleXYSeries(
                state.getValue().getValues(),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                currency.getDisplayName()
        );


        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);


        // add an "dash" effect to the series2 line:

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));


        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(state.getValue().getLabels().get(i));
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        plot.invalidate();
        currencyPrice = state.getValue().getCurrentPrice();
        binding.price.setText("Цена: " + currencyPrice + " RUB");
    }


    private void goSearchStocks() {
        graphRepository.search(9, currency.getApiName());
    }
}
