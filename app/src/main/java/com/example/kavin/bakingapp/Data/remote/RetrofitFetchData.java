package com.example.kavin.bakingapp.Data.remote;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kavin on 2/19/2018.
 */

//BASE_URL WEBSITE: https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json

public class RetrofitFetchData {
    private static Retrofit retrofit = null;
    public static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
