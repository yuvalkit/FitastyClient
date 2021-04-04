package com.fitastyclient.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    static public String serverUrl = "http://3.142.151.208:8080";

    static private Api getRetrofitApiByFactory(GsonConverterFactory factory) {
        return new Retrofit.Builder()
                .baseUrl(HttpManager.serverUrl)
                .addConverterFactory(factory)
                .build()
                .create(Api.class);
    }

    static public Api getRetrofitApi() {
        return getRetrofitApiByFactory(GsonConverterFactory.create());
    }

    static public Api getRetrofitApiWithNulls() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return getRetrofitApiByFactory(GsonConverterFactory.create(gson));
    }
}
