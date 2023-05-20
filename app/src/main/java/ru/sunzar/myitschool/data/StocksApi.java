package ru.sunzar.myitschool.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface StocksApi {
    //    @GET("/{date}/currencies/usd.json")
//    Call<StocksSearchResponse> getSearchResult(
//            @Path("date") String date
//    );
    @GET("/gh/fawazahmed0/currency-api@1/{date}/currencies/rub.json")
    Call<StocksSearchResponse> getSearchResult(
            @Path("date") String date
    );
//    Call<StocksSearchResponse> getSearchResult(
//            @Path("") String apikey
//            )
    //TODO: https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/btc/usd.json
}
