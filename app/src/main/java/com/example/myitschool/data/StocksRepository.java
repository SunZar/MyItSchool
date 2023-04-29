package com.example.myitschool.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myitschool.utils.Resource;
import com.example.myitschool.utils.RetrofitCallback;
import com.example.myitschool.utils.Utils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StocksRepository {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final StocksApi api = retrofit.create(StocksApi.class);
    private final MutableLiveData<Resource<StocksSearchResponse>> mutableStockSearchLiveData = new MutableLiveData<>();

    public LiveData<Resource<StocksSearchResponse>> stockSearchLiveData = mutableStockSearchLiveData;

    public void search(String request) {
        api.getSearchResult(/*request*/).enqueue(
                new RetrofitCallback<StocksSearchResponse>() {
                    @Override
                    public void onResponse(Resource<StocksSearchResponse> data) {
                        mutableStockSearchLiveData.setValue(data);
                    }
                }
        );
    }
}
