package com.fitastyclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @GET(".")
    Call<ResponseBody> isCorrectUsernameAndPassword(
            @Query("username") String username,
            @Query("password") String password
    );
}
