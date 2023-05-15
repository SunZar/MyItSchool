package com.example.myitschool.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface StocksApi {
    @GET("/gh/fawazahmed0/currency-api@1/{date}/currencies/usd.json")
    Call<StocksSearchResponse> getSearchResult(
            @Path("date") String date
    );
}
