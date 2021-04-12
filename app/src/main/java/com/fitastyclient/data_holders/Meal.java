package com.fitastyclient.data_holders;

import com.fitastyclient.Utils;
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

    public int getMealId() {
        return this.mealId;
    }

    public List<ShortDish> getDishes() {
        return this.dishes;
    }

    public List<ShortIngredient> getIngredients() {
        return this.ingredients;
    }

    public Double getFat() {
        return this.fat;
    }

    public Double getCarb() {
        return this.carb;
    }

    public Double getFiber() {
        return this.fiber;
    }

    public Double getProtein() {
        return this.protein;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public CalorieInfo getCalorieInfoFromFields() {
        return new CalorieInfo(this.carb, this.fiber, this.fat, this.protein);
    }

    public double getCaloriesFromFields() {
        return Utils.getCalories(this.fat, this.carb, this.protein);
    }

    public void replaceByMeal(Meal meal) {
        this.dishes = meal.getDishes();
        this.ingredients = meal.getIngredients();
        this.fat = meal.getFat();
        this.carb = meal.getCarb();
        this.fiber = meal.getFiber();
        this.protein = meal.getProtein();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Meal other = (Meal) obj;
        return this.mealId == other.getMealId()
                && this.dishes.equals(other.getDishes())
                && this.ingredients.equals(other.getIngredients())
                && this.fat.equals(other.getFat())
                && this.carb.equals(other.getCarb())
                && this.fiber.equals(other.getFiber())
                && this.protein.equals(other.getProtein());
    }

}
