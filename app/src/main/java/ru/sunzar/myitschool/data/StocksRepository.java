package ru.sunzar.myitschool.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.sunzar.myitschool.utils.Resource;
import ru.sunzar.myitschool.utils.RetrofitCallback;
import ru.sunzar.myitschool.utils.Utils;

public class StocksRepository {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final StocksApi api = retrofit.create(StocksApi.class);
    private final MutableLiveData<Resource<StocksSearchResponse>> mutableStockSearchLiveData = new MutableLiveData<>();

    public LiveData<Resource<StocksSearchResponse>> stockSearchLiveData = mutableStockSearchLiveData;

    public void search(String request) {
        api.getSearchResult(request).enqueue(
                new RetrofitCallback<StocksSearchResponse>() {
                    @Override
                    public void onResponse(Resource<StocksSearchResponse> data) {
                        mutableStockSearchLiveData.setValue(data);
                    }
                }
        );
    }
}
