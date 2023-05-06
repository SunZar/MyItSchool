package com.example.myitschool.ui;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.myitschool.R;
import com.example.myitschool.data.StocksRepository;
import com.example.myitschool.data.StocksSearchResponse;
import com.example.myitschool.databinding.ActivityMainBinding;
import com.example.myitschool.utils.Resource;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

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
    private XYPlot plot;

    //String[] domainLabels;
    ArrayList<String> domainLabels = new ArrayList<String>();
    ArrayList<Double> series1Numbers = new ArrayList<Double>();
    //Number[] series2Numbers;



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

        plot = (XYPlot) binding.plot;
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

        // initialize our XYPlot reference:


        // create a couple arrays of y-values to plot:

        series1Numbers.add(state.getValue().getCurrencies().get("btc"));


        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)







    }


    private void goSearchStocks() {
        String searchRequest = binding.currency.getText().toString().trim();

        long longDate = System.currentTimeMillis();
        long longDateI = longDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(longDate);
        binding.text.setText(sDate);
        for (int i = 0; i < 10; i++) {
            longDateI = longDateI - 86_400_000;
            sDate = sdf.format(longDateI);
            domainLabels.add(sDate);
            repository.search(sDate);
        }

        XYSeries series1 = new SimpleXYSeries(
                series1Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");


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
                return toAppendTo.append(domainLabels.get(i));
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        plot.invalidate();

    }
}