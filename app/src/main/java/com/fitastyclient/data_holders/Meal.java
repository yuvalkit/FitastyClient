package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Meal implements Serializable {

    @SerializedName("meal_id")
    private int mealId;
    @SerializedName("dishes")
    private List<ShortDish> dishes;
    @SerializedName("ingredients")
    private List<ShortIngredient> ingredients;
    @SerializedName("fat")
    private Double fat;
    @SerializedName("carb")
    private Double carb;
    @SerializedName("fiber")
    private Double fiber;
    @SerializedName("protein")
    private Double protein;

    public Meal(int mealId, List<ShortDish> dishes, List<ShortIngredient> ingredients,
                Double fat, Double carb, Double fiber, Double protein) {
        this.mealId = mealId;
        this.dishes = dishes;
        this.ingredients = ingredients;
        this.fat = fat;
        this.carb = carb;
        this.fiber = fiber;
        this.protein = protein;
    }

    public Meal(int mealId, List<ShortDish> dishes, List<ShortIngredient> ingredients) {
        this(mealId, dishes, ingredients, null, null, null, null);
    }

}
