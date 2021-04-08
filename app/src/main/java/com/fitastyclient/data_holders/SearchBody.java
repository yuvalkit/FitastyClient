package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SearchBody implements Serializable {

    @SerializedName("name_begin")
    private String nameBegin;
    @SerializedName("max_fat")
    private Double maxFat;
    @SerializedName("max_carb")
    private Double maxCarb;
    @SerializedName("max_fiber")
    private Double maxFiber;
    @SerializedName("max_protein")
    private Double maxProtein;
    @SerializedName("min_fat")
    private Double minFat;
    @SerializedName("min_carb")
    private Double minCarb;
    @SerializedName("min_fiber")
    private Double minFiber;
    @SerializedName("min_protein")
    private Double minProtein;
    @SerializedName("is_vegan")
    private boolean isVegan;
    @SerializedName("is_vegetarian")
    private boolean isVegetarian;
    @SerializedName("is_gluten_free")
    private boolean isGlutenFree;
    @SerializedName("is_lactose_free")
    private boolean isLactoseFree;

    public SearchBody(String nameBegin, Double maxFat, Double maxCarb, Double maxFiber,
                      Double maxProtein, Double minFat, Double minCarb, Double minFiber,
                      Double minProtein, boolean isVegan, boolean isVegetarian,
                      boolean isGlutenFree, boolean isLactoseFree) {
        this.nameBegin = nameBegin;
        this.maxFat = maxFat;
        this.maxCarb = maxCarb;
        this.maxFiber = maxFiber;
        this.maxProtein = maxProtein;
        this.minFat = minFat;
        this.minCarb = minCarb;
        this.minFiber = minFiber;
        this.minProtein = minProtein;
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isGlutenFree = isGlutenFree;
        this.isLactoseFree = isLactoseFree;
    }

    public SearchBody(String nameBegin, boolean isVegan, boolean isVegetarian,
                      boolean isGlutenFree, boolean isLactoseFree,
                      NutritionFactsFilter factsFilter) {
        this(nameBegin, factsFilter.getMaxFat(), factsFilter.getMaxCarb(),
                factsFilter.getMaxFiber(), factsFilter.getMaxProtein(), factsFilter.getMinFat(),
                factsFilter.getMinCarb(), factsFilter.getMinFiber(), factsFilter.getMinProtein(),
                isVegan, isVegetarian, isGlutenFree, isLactoseFree);
    }

}
