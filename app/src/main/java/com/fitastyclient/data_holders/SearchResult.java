package com.fitastyclient.data_holders;

import com.fitastyclient.data_holders.ShortDish;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {

    @SerializedName("dishes")
    private List<ShortDish> dishes;
    @SerializedName("ingredients")
    private List<ShortIngredient> ingredients;

    public SearchResult(List<ShortDish> dishes,
                        List<ShortIngredient> ingredients) {
        this.dishes = dishes;
        this.ingredients = ingredients;
    }

    public List<ShortDish> getDishes() {
        return this.dishes;
    }

    public List<ShortIngredient> getIngredients() {
        return this.ingredients;
    }

}
