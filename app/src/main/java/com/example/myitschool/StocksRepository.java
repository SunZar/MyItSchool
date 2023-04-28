package com.example.myitschool;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StocksRepository {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final StocksApi api = retrofit.create(StocksApi.class);

    private List<StocksShortData> shortStocksList = new ArrayList<>();
    private OnLoadingStocksState onLoadingStocksState = null;

    public void search(String request) {
        api.getSearchResult(request).enqueue(new Callback<StocksSearchResponse>() {
            @Override
            public void onResponse(Call<StocksSearchResponse> call, Response<StocksSearchResponse> response) {
                if (onLoadingStocksState != null && response.isSuccessful()) {
                    StocksSearchResponse body = response.body();
                    if (body != null) {
                        onLoadingStocksState.changeState(
                                new OnLoadingStocksState.State.Success(body.getSearch())
                        );
                    }
                }
            }

            @Override
            public void onFailure(Call<StocksSearchResponse> call, Throwable t) {

            }
        });
    }
//    public void search(String request) {
//        api.getSearchResult("API KEY", request).enqueue(new);
//    }

    public void setOnLoadingStocksState(OnLoadingStocksState state) {
        onLoadingStocksState = state;
    }

    public void removeOnLoadingStocksState() {
        onLoadingStocksState = null;
    }
}
