package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;

public class AmountShortIngredient extends AbstractShortIngredient {

    @SerializedName("amount")
    private int amount;

    public AmountShortIngredient(String ingredientName, int amount) {
        super(ingredientName);
        this.amount = amount;
    }
}
