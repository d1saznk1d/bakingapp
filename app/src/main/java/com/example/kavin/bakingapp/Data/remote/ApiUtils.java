package com.example.kavin.bakingapp.Data.remote;

/**
 * Created by kavin on 2/19/2018.
 */

public class ApiUtils {

    public static MyApiEndpointInterface getAPIService() {
        return RetrofitFetchData.getClient().create(MyApiEndpointInterface.class);
    }
}
