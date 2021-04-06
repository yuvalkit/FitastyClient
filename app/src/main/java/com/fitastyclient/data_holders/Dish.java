package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Dish implements Serializable {

    @SerializedName("dish_name")
    private String dishName;
    @SerializedName("fat")
    private double fat;
    @SerializedName("carb")
    private double carb;
    @SerializedName("fiber")
    private double fiber;
    @SerializedName("protein")
    private double protein;
    @SerializedName("ingredients")
    private List<ShortIngredient> ingredients;

    public Dish(String dishName, double fat, double carb, double fiber,
                double protein, List<ShortIngredient> ingredients) {
        this.dishName = dishName;
        this.fat = fat;
        this.carb = carb;
        this.fiber = fiber;
        this.protein = protein;
        this.ingredients = ingredients;
    }

    public String getDishName() {
        return this.dishName;
    }

    public double getFat() {
        return this.fat;
    }

    public double getCarb() {
        return this.carb;
    }

    public double getFiber() {
        return this.fiber;
    }

    public double getProtein() {
        return this.protein;
    }

    public List<ShortIngredient> getIngredients() {
        return this.ingredients;
    }

}
