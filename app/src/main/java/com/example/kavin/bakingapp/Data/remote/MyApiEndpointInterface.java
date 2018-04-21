package com.example.kavin.bakingapp.Data.remote;

import com.example.kavin.bakingapp.Data.BakingAppsDataModel;
import com.example.kavin.bakingapp.Data.IngredientsDataModel;
import com.example.kavin.bakingapp.Data.StepDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kavin on 2/19/2018.
 */

public interface MyApiEndpointInterface {
    ArrayList<BakingAppsDataModel> bakingAppsDataModels = new ArrayList<>();

    //BASE_URL WEBSITE: https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<BakingAppsDataModel>> getBakingData();
}
