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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ShortDish other = (ShortDish) obj;
        return this.dishName.equals(other.getDishName())
                && this.percent.equals(other.getPercent());
    }

}
