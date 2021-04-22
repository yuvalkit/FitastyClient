package com.fitastyclient;

import com.fitastyclient.data_holders.Account;
import com.fitastyclient.data_holders.DietDiary;
import com.fitastyclient.data_holders.DishToInsert;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.data_holders.SearchBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Api {

    // ====================================================
    // users API
    // ====================================================

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

    @PUT("/users/update_account")
    Call<ResponseBody> updateAccount(
            @Query("prev_username") String username,
            @Body Account account
    );

    @DELETE("/users/delete_account")
    Call<ResponseBody> deleteAccount(
            @Query("username") String username
    );

    @GET("/users/get_calorie_info")
    Call<ResponseBody> getCalorieInfo(
            @Query("username") String username
    );

    // ====================================================
    // foods API
    // ====================================================

    @POST("/foods/insert_ingredient")
    Call<ResponseBody> insertNewIngredient(
            @Body Ingredient ingredient
    );

    @POST("/foods/get_foods")
    Call<ResponseBody> getFoods(
            @Query("include_dishes") boolean includeDishes,
            @Query("include_ingredients") boolean includeIngredients,
            @Body SearchBody searchBody
    );

    @GET("/foods/get_ingredient_info")
    Call<ResponseBody> getIngredientInfo(
            @Query("ingredient_name") String ingredientName
    );

    @GET("/foods/get_dish_info")
    Call<ResponseBody> getDishInfo(
            @Query("dish_name") String dishName
    );

    @POST("/foods/insert_dish")
    Call<ResponseBody> insertNewDish(
            @Body DishToInsert dishToInsert
    );

    // ====================================================
    // diet_diaries API
    // ====================================================

    @POST("/diet_diaries/insert_diet_diary")
    Call<ResponseBody> insertDietDiary(
            @Query("username") String username,
            @Body DietDiary dietDiary
    );

    @GET("/diet_diaries/get_diet_diaries")
    Call<ResponseBody> getDietDiaries(
            @Query("username") String username
    );

    @GET("/diet_diaries/get_diet_diary")
    Call<ResponseBody> getDietDiary(
            @Query("username") String username,
            @Query("diet_diary_name") String dietDiaryName
    );

    @PUT("/diet_diaries/update_diet_diary")
    Call<ResponseBody> updateDietDiary(
            @Query("username") String username,
            @Query("prev_diet_diary_name") String prevDietDiaryName,
            @Body DietDiary dietDiary
    );

    @DELETE("/diet_diaries/delete_diet_diary")
    Call<ResponseBody> deleteDietDiary(
            @Query("username") String username,
            @Query("diet_diary_name") String dietDiaryName
    );

    // ====================================================
    // other
    // ====================================================

    @POST("/json_echo")
    Call<ResponseBody> jsonEcho(
            @Body DietDiary body
    );

}
