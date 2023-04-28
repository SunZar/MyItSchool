package com.example.myitschool;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StocksApi {
    @GET("gh/fawazahmed0/currency-api@1/latest/currencies/btc/usd.json")
    Call<StocksSearchResponse> getSearchResult();
//    Call<StocksSearchResponse> getSearchResult(
//            @Path("") String apikey
//            )
        //TODO: https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/btc/usd.json
}
