package com.fitastyclient.data_holders;

import com.fitastyclient.Utils;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ShortIngredient implements Serializable {

    @SerializedName("ingredient_name")
    protected String ingredientName;
    @SerializedName("is_liquid")
    private Boolean isLiquid;
    @SerializedName("amount")
    private Double amount;

    public ShortIngredient(String ingredientName, Boolean isLiquid, Double amount) {
        this.ingredientName = ingredientName;
        this.isLiquid = isLiquid;
        this.amount = amount;
    }

    public ShortIngredient(String ingredientName, Boolean isLiquid) {
        this(ingredientName, isLiquid, null);
    }

    public ShortIngredient(String ingredientName, Double amount) {
        this(ingredientName, null, amount);
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    public Boolean getIsLiquid() {
        return this.isLiquid;
    }

    public Double getAmount() {
        return this.amount;
    }

    public String getUnits() {
        if (this.isLiquid) return Utils.ML;
        else return Utils.GRAM;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ShortIngredient other = (ShortIngredient) obj;
        if ((this.isLiquid != null) && (other.getIsLiquid() != null)) {
            if (!this.isLiquid.equals(other.getIsLiquid())) {
                return false;
            }
        }
        if ((this.amount != null) && (other.getAmount() != null)) {
            if (!this.amount.equals(other.getAmount())) {
                return false;
            }
        }
        return this.ingredientName.equals(other.getIngredientName());
    }
}
