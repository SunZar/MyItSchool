package com.example.myitschool.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface StocksApi {
//    @GET("/{date}/currencies/usd.json")
//    Call<StocksSearchResponse> getSearchResult(
//            @Path("date") String date
//    );
@GET("/gh/fawazahmed0/currency-api@1/2023-04-28/currencies/usd.json")
Call<StocksSearchResponse> getSearchResult(

);
//    Call<StocksSearchResponse> getSearchResult(
//            @Path("") String apikey
//            )
        //TODO: https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/btc/usd.json
}
