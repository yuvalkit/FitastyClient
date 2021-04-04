package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class DishToInsert implements Serializable {

    @SerializedName("dish_name")
    private String dishName;
    @SerializedName("ingredients")
    private List<ShortIngredient> ingredients;
    @SerializedName("dishes")
    private List<ShortDish> dishes;

    public DishToInsert(String dishName, List<ShortIngredient> ingredients,
                        List<ShortDish> dishes) {
        this.dishName = dishName;
        this.ingredients = ingredients;
        this.dishes = dishes;
    }
}
