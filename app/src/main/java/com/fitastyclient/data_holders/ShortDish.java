package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShortDish implements Serializable {

    @SerializedName("dish_name")
    private String dishName;

    @SerializedName("percent")
    private Double percent;

    public ShortDish(String dishName, Double percent) {
        this.dishName = dishName;
        this.percent = percent;
    }

    public ShortDish(String dishName) {
        this.dishName = dishName;
        this.percent = null;
    }

    public String getDishName() {
        return this.dishName;
    }

    public double getPercent() {
        return this.percent;
    }
}
