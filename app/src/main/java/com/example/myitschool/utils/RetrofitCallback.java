package com.example.myitschool.utils;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallback<T> implements Callback<T> {

    public RetrofitCallback() {
        onResponse(new Resource.Loading<>());
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            onResponse(new Resource.Success<>(response.body()));
        } else {
            Log.d("LOG", "" + response.code());
            onResponse(new Resource.Error<>());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d("LOG", t.getMessage());
        onResponse(new Resource.Error<>());
    }

    public abstract void onResponse(Resource<T> data);
}
