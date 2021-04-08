package com.fitastyclient.data_holders;

import java.io.Serializable;

public class NutritionFactsFilter implements Serializable {

    private Double maxFat;
    private Double maxCarb;
    private Double maxFiber;
    private Double maxProtein;
    private Double minFat;
    private Double minCarb;
    private Double minFiber;
    private Double minProtein;

    public NutritionFactsFilter(Double maxFat, Double maxCarb, Double maxFiber,
                                Double maxProtein, Double minFat, Double minCarb,
                                Double minFiber,  Double minProtein) {
        this.maxFat = maxFat;
        this.maxCarb = maxCarb;
        this.maxFiber = maxFiber;
        this.maxProtein = maxProtein;
        this.minFat = minFat;
        this.minCarb = minCarb;
        this.minFiber = minFiber;
        this.minProtein = minProtein;
    }

    public NutritionFactsFilter(CalorieInfo maxCalorieInfo) {
        this(maxCalorieInfo.getFat(), maxCalorieInfo.getCarb(), maxCalorieInfo.getFiber(),
                maxCalorieInfo.getProtein(), null, null, null, null);
    }

    public NutritionFactsFilter() {
        this(null, null, null, null, null, null, null, null);
    }

    public Double getMaxFat() {
        return this.maxFat;
    }

    public Double getMaxCarb() {
        return this.maxCarb;
    }

    public Double getMaxFiber() {
        return this.maxFiber;
    }

    public Double getMaxProtein() {
        return this.maxProtein;
    }

    public Double getMinFat() {
        return this.minFat;
    }

    public Double getMinCarb() {
        return this.minCarb;
    }

    public Double getMinFiber() {
        return this.minFiber;
    }

    public Double getMinProtein() {
        return this.minProtein;
    }

}
