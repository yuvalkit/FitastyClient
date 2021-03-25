package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;

public class ShortIngredient extends AbstractShortIngredient {

    @SerializedName("is_liquid")
    private boolean isLiquid;

    @SerializedName("amount")
    private int amount;

    public ShortIngredient(String ingredientName, boolean isLiquid, int amount) {
        super(ingredientName);
        this.isLiquid = isLiquid;
        this.amount = amount;
    }

    public boolean getIsLiquid() {
        return this.isLiquid;
    }

    public int getAmount() {
        return this.amount;
    }
}
