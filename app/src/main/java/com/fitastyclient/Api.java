package com.fitastyclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("/users/log_in")
    Call<ResponseBody> isCorrectUsernameAndPassword(
            @Query("username") String username,
            @Query("password") String password
    );

    @GET("/users/check_username")
    Call<ResponseBody> isUsernameAvailable(
            @Query("username") String username
    );

    @POST("/users/insert_account")
    Call<ResponseBody> insertNewAccount(
            @Body Account account
    );
}
