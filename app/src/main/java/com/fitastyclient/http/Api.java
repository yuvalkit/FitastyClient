package com.fitastyclient.http;

import com.fitastyclient.data_holders.Account;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.DishToInsert;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.data_holders.NameExistObject;
import com.fitastyclient.data_holders.SearchBody;
import com.fitastyclient.data_holders.SearchResult;
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
    Call<NameExistObject> isUsernameAvailable(
            @Query("username") String username
    );

    @POST("/users/insert_account")
    Call<NameExistObject> insertNewAccount(
            @Body Account account
    );

    @GET("/users/get_account_info")
    Call<Account> getAccountInformation(
            @Query("username") String username
    );

    @PUT("/users/update_account")
    Call<NameExistObject> updateAccount(
            @Query("prev_username") String username,
            @Body Account account
    );

    @DELETE("/users/delete_account")
    Call<ResponseBody> deleteAccount(
            @Query("username") String username
    );

    // ====================================================
    // foods API
    // ====================================================

    @POST("/foods/insert_ingredient")
    Call<NameExistObject> insertNewIngredient(
            @Body Ingredient ingredient
    );

    @POST("/foods/get_foods")
    Call<SearchResult> getFoods(
            @Query("include_dishes") boolean includeDishes,
            @Query("include_ingredients") boolean includeIngredients,
            @Body SearchBody searchBody
    );

    @GET("/foods/get_ingredient_info")
    Call<Ingredient> getIngredientInfo(
            @Query("ingredient_name") String ingredientName
    );

    @GET("/foods/get_dish_info")
    Call<Dish> getDishInfo(
            @Query("dish_name") String dishName
    );

    @POST("/foods/insert_dish")
    Call<NameExistObject> insertNewDish(
            @Body DishToInsert dishToInsert
    );

    // ====================================================
    // echo
    // ====================================================

    @POST("/json_echo")
    Call<ResponseBody> jsonEcho(
            @Body SearchBody searchBody
    );
}
