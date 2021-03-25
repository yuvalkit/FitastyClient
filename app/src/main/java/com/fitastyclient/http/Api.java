package com.fitastyclient.http;

import com.fitastyclient.data_holders.Account;
import com.fitastyclient.data_holders.Ingredient;

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

    @GET("/users/get_account_info")
    Call<ResponseBody> getAccountInformation(
            @Query("username") String username
    );

    @POST("/users/update_account")
    Call<ResponseBody> updateAccount(
            @Query("prev_username") String username,
            @Body Account account
    );

    @POST("/food/insert_ingredient")
    Call<ResponseBody> insertNewIngredient(
            @Body Ingredient ingredient
    );
}
