package com.fitastyclient;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DietType implements Serializable {

    @SerializedName("carb")
    private double carb;
    @SerializedName("fat")
    private double fat;
    @SerializedName("protein")
    private double protein;

    DietType(double carb, double fat, double protein) {
        this.carb = carb;
        this.fat = fat;
        this.protein = protein;
    }

    public double getCarb() {
        return this.carb;
    }

    public double getFat() {
        return this.fat;
    }

    public double getProtein() {
        return this.protein;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        DietType other = (DietType) obj;
        return this.carb == other.getCarb()
                && this.fat == other.getFat()
                && this.protein == other.getProtein();
    }
}
