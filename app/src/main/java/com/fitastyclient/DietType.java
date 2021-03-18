package com.fitastyclient;

import com.google.gson.annotations.SerializedName;

public class DietType {

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
}
