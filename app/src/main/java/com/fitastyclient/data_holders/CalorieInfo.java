package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CalorieInfo implements Serializable {

    @SerializedName("carb")
    private double carb;
    @SerializedName("fiber")
    private double fiber;
    @SerializedName("fat")
    private double fat;
    @SerializedName("protein")
    private double protein;

    public CalorieInfo(double carb, double fiber, double fat, double protein) {
        this.carb = carb;
        this.fiber = fiber;
        this.fat = fat;
        this.protein = protein;
    }

    public CalorieInfo() {
        this(0, 0, 0, 0);
    }

    public double getCarb() {
        return this.carb;
    }

    public double getFiber() {
        return this.fiber;
    }

    public double getFat() {
        return this.fat;
    }

    public double getProtein() {
        return this.protein;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void addOtherCalorieInfo(CalorieInfo otherCalorieInfo) {
        this.carb += otherCalorieInfo.getCarb();
        this.fiber += otherCalorieInfo.getFiber();
        this.fat += otherCalorieInfo.getFat();
        this.protein += otherCalorieInfo.getProtein();
    }

    public void subtractOtherCalorieInfo(CalorieInfo otherCalorieInfo) {
        this.carb -= otherCalorieInfo.getCarb();
        this.fiber -= otherCalorieInfo.getFiber();
        this.fat -= otherCalorieInfo.getFat();
        this.protein -= otherCalorieInfo.getProtein();
    }
}
