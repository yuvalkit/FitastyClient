package com.fitastyclient.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    static public String serverUrl = "http://3.142.151.208:8080";

    static public Api getRetrofitApi() {
        return new Retrofit.Builder()
                .baseUrl(HttpManager.serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);
    }
}
