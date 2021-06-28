package com.fitastyclient.data_holders;

import com.fitastyclient.Utils;
import java.io.Serializable;

public class NutritionFactsFilter implements Serializable {

    private Double fat;
    private Double carb;
    private Double fiber;
    private Double protein;
    private Double minPercent;
    private Double maxPercent;

    public NutritionFactsFilter(Double fat, Double carb, Double fiber, Double protein,
                                Double minPercent, Double maxPercent) {
        this.fat = fat;
        this.carb = carb;
        this.fiber = fiber;
        this.protein = protein;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
        zeroMaxFields();
    }

    public NutritionFactsFilter(CalorieInfo calorieInfo, Double minPercent, Double maxPercent) {
        this(calorieInfo.getFat(), calorieInfo.getCarb(), calorieInfo.getFiber(),
                calorieInfo.getProtein(), minPercent, maxPercent);
    }

    public NutritionFactsFilter() {
        this(null, null, null, null, Utils.FACTS_FILTER_DEFAULT_MIN_PERCENT,
                Utils.FACTS_FILTER_DEFAULT_MAX_PERCENT);
    }

    public Double getFat() {
        return this.fat;
    }

    public Double getCarb() {
        return this.carb;
    }

    public Double getFiber() {
        return this.fiber;
    }

    public Double getProtein() {
        return this.protein;
    }

    public Double getMinPercent() {
        return this.minPercent;
    }

    public Double getMaxPercent() {
        return this.maxPercent;
    }

    public void subtractMaxValuesByCalorieInfo(CalorieInfo calorieInfo) {
        if (this.fat != null) this.fat -= calorieInfo.getFat();
        if (this.carb != null) this.carb -= calorieInfo.getCarb();
        if (this.fiber != null) this.fiber -= calorieInfo.getFiber();
        if (this.protein != null) this.protein -= calorieInfo.getProtein();
        zeroMaxFields();
    }

    private void zeroMaxFields() {
        this.fat = Utils.zeroMax(this.fat);
        this.carb = Utils.zeroMax(this.carb);
        this.fiber = Utils.zeroMax(this.fiber);
        this.protein = Utils.zeroMax(this.protein);
    }

}
