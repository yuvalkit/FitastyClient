package com.fitastyclient;

import retrofit2.Retrofit;

public class HttpManager {

    static public String serverUrl = "http://3.142.151.208:8080";

    static public Api getRetrofitApi() {
        return new Retrofit.Builder()
                .baseUrl(HttpManager.serverUrl)
                .build()
                .create(Api.class);
    }
}
