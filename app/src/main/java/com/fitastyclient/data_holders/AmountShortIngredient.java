package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;

public class AmountShortIngredient extends AbstractShortIngredient {

    @SerializedName("amount")
    private int amount;

    public AmountShortIngredient(String ingredientName, boolean isLiquid, int amount) {
        super(ingredientName, isLiquid);
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }
}
