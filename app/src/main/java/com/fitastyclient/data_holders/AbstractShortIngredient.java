package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;

public abstract class AbstractShortIngredient {

    @SerializedName("ingredient_name")
    protected String ingredientName;

    @SerializedName("is_liquid")
    private boolean isLiquid;

    public AbstractShortIngredient(String ingredientName, boolean isLiquid) {
        this.ingredientName = ingredientName;
        this.isLiquid = isLiquid;
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    public boolean getIsLiquid() {
        return this.isLiquid;
    }
}
