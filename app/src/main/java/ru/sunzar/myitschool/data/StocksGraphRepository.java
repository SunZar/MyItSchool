package ru.sunzar.myitschool.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.sunzar.myitschool.utils.Resource;
import ru.sunzar.myitschool.utils.RetrofitCallback;
import ru.sunzar.myitschool.utils.Utils;

public class StocksGraphRepository {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final StocksApi api = retrofit.create(StocksApi.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final MutableLiveData<Resource<StockGraphData>> mutableStockGraphDataLiveData = new MutableLiveData<>();

    public LiveData<Resource<StockGraphData>> stockGraphDataLiveData = mutableStockGraphDataLiveData;

    public void search(long period, final String currencies) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        long day = System.currentTimeMillis() - 30_600_000 - TimeUnit.DAYS.toMillis(period - 1);

        final Map<String, Resource<StocksSearchResponse>> results = new TreeMap<>();

        for (long i = 0; i < period; i++) {
            final String formatDate = sdf.format(day);
            getStocksFromDate(formatDate, new RetrofitCallback<StocksSearchResponse>() {
                @Override
                public void onResponse(Resource<StocksSearchResponse> data) {
                    results.put(formatDate, data);
                    checkData(results, period, currencies);
                }
            });
            day += TimeUnit.DAYS.toMillis(1);
        }

    }

    private void checkData(Map<String, Resource<StocksSearchResponse>> results, long period, String currencies) {
        if (results.size() < period) return;
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Float> values = new ArrayList<>();
        results.forEach((date, result) -> {
            if (result instanceof Resource.Success) {
                labels.add(date);
                double temp = 1 / ((Resource.Success<StocksSearchResponse>) result).getValue()
                        .getCurrencies()
                        .get(currencies);
                if (currencies == StocksData.Currency.ETH.getApiName()) {
                    values.add(
                            (float) (Math.ceil((temp * 100 + (Math.random() * temp * 100 / 100)) * 100) / 100)
                    );
                } else {
                    values.add(
                            (float) (Math.ceil((temp + (Math.random() * temp / 100)) * 100) / 100)
                    );
                }
//                values.add(Math.ceil(
//                        ((Resource.Success<StocksSearchResponse>) result)
//                                .getValue()
//                                .getCurrencies()
//                                .get(currencies)
//                ));
            } else if (result instanceof Resource.Loading) {
                mutableStockGraphDataLiveData.setValue(new Resource.Loading<>());
            } else if (result instanceof Resource.Error) {
                mutableStockGraphDataLiveData.setValue(new Resource.Error<>());
            }
        });
        if (labels.size() != period) return;
        mutableStockGraphDataLiveData.setValue(new Resource.Success<>(
                new StockGraphData(
                        labels,
                        values,
                        values.get(values.size() - 1)
                )
        ));
    }

    private void getStocksFromDate(
            String date,
            final RetrofitCallback<StocksSearchResponse> callback
    ) {
        api.getSearchResult(date).enqueue(callback);
    }
}
