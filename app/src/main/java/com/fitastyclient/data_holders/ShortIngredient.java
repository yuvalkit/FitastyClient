package com.fitastyclient.data_holders;
import com.fitastyclient.Utils;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ShortIngredient implements Serializable {

    @SerializedName("ingredient_name")
    protected String ingredientName;

    @SerializedName("is_liquid")
    private Integer isLiquid;

    @SerializedName("amount")
    private Double amount;

    public ShortIngredient(String ingredientName, Integer isLiquid, Double amount) {
        this.ingredientName = ingredientName;
        this.isLiquid = isLiquid;
        this.amount = amount;
    }

    public ShortIngredient(String ingredientName, Integer isLiquid) {
        this.ingredientName = ingredientName;
        this.isLiquid = isLiquid;
        this.amount = null;
    }

    public ShortIngredient(String ingredientName, Double amount) {
        this(ingredientName, null, amount);
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    public Boolean getIsLiquid() {
        return this.isLiquid == 1;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getUnits() {
        if (this.isLiquid == 1) return Utils.ML;
        else return Utils.GRAM;
    }
}
