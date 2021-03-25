package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ingredient implements Serializable {

    @SerializedName("name")
    private String name;
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
    @SerializedName("is_gluten-free")
    private boolean isGlutenFree;
    @SerializedName("is_lactose-free")
    private boolean isLactoseFree;
    @SerializedName("serving")
    private double serving;

    public Ingredient(String name, boolean isLiquid, double fat, double carb, double fiber,
                      double protein, boolean isVegan, boolean isVegetarian, boolean isGlutenFree,
                      boolean isLactoseFree, double serving) {
        this.name = name;
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

}
