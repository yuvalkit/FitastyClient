package com.fitastyclient.data_holders;

import com.fitastyclient.Utils;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Ingredient implements Serializable {

    @SerializedName("ingredient_name")
    private String ingredientName;
    @SerializedName("is_liquid")
    private boolean isLiquid;
    @SerializedName("fat")
    private double fat;
    @SerializedName("carb")
    private double carb;
    @SerializedName("fiber")
    private double fiber;
    @SerializedName("protein")
    private double protein;
    @SerializedName("is_vegan")
    private boolean isVegan;
    @SerializedName("is_vegetarian")
    private boolean isVegetarian;
    @SerializedName("is_gluten_free")
    private boolean isGlutenFree;
    @SerializedName("is_lactose_free")
    private boolean isLactoseFree;
    @SerializedName("serving")
    private double serving;

    public Ingredient(String ingredientName, boolean isLiquid, double fat, double carb,
                      double fiber, double protein, boolean isVegan, boolean isVegetarian,
                      boolean isGlutenFree, boolean isLactoseFree, double serving) {
        this.ingredientName = ingredientName;
        this.isLiquid = isLiquid;
        this.fat = fat;
        this.carb = carb;
        this.fiber = fiber;
        this.protein = protein;
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isGlutenFree = isGlutenFree;
        this.isLactoseFree = isLactoseFree;
        this.serving = serving;
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    public boolean getIsLiquid() {
        return this.isLiquid;
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

    public boolean getIsVegan() {
        return this.isVegan;
    }

    public boolean getIsVegetarian() {
        return this.isVegetarian;
    }

    public boolean getIsGlutenFree() {
        return this.isGlutenFree;
    }

    public boolean getIsLactoseFree() {
        return this.isLactoseFree;
    }

    public double getServing() {
        return this.serving;
    }

    public String getUnits() {
        if (this.isLiquid) return Utils.ML;
        else return Utils.GRAM;
    }
}
