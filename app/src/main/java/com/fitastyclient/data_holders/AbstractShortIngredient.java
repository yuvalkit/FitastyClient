package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public abstract class AbstractShortIngredient implements Serializable {

    @SerializedName("ingredient_name")
    protected String ingredientName;


    public AbstractShortIngredient(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientName() {
        return this.ingredientName;
    }
}
