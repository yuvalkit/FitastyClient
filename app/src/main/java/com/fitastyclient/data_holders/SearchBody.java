package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SearchBody implements Serializable {

    @SerializedName("name_begin")
    private String nameBegin;
    @SerializedName("fat")
    private Double fat;
    @SerializedName("carb")
    private Double carb;
    @SerializedName("fiber")
    private Double fiber;
    @SerializedName("protein")
    private Double protein;
    @SerializedName("is_vegan")
    private boolean isVegan;
    @SerializedName("is_vegetarian")
    private boolean isVegetarian;
    @SerializedName("is_gluten_free")
    private boolean isGlutenFree;
    @SerializedName("is_lactose_free")
    private boolean isLactoseFree;
    @SerializedName("min_percent")
    private Double minPercent;
    @SerializedName("max_percent")
    private Double maxPercent;

    public SearchBody(String nameBegin, Double fat, Double carb, Double fiber, Double protein,
                      boolean isVegan, boolean isVegetarian, boolean isGlutenFree,
                      boolean isLactoseFree, Double minPercent, Double maxPercent) {
        this.nameBegin = nameBegin;
        this.fat = fat;
        this.carb = carb;
        this.fiber = fiber;
        this.protein = protein;
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isGlutenFree = isGlutenFree;
        this.isLactoseFree = isLactoseFree;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
    }

    public SearchBody(String nameBegin, boolean isVegan, boolean isVegetarian,
                      boolean isGlutenFree, boolean isLactoseFree,
                      NutritionFactsFilter factsFilter) {
        this(nameBegin, factsFilter.getFat(), factsFilter.getCarb(), factsFilter.getFiber(),
                factsFilter.getProtein(), isVegan, isVegetarian, isGlutenFree, isLactoseFree,
                factsFilter.getMinPercent(), factsFilter.getMaxPercent());
    }
}
