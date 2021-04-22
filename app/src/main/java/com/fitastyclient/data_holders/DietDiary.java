package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class DietDiary implements Serializable {

    @SerializedName("diet_diary_name")
    private String dietDiaryName;
    @SerializedName("meals")
    private List<Meal> meals;

    public DietDiary(String dietDiaryName, List<Meal> meals) {
        this.dietDiaryName = dietDiaryName;
        this.meals = meals;
    }

    public DietDiary(String dietDiaryName) {
        this(dietDiaryName, null);
    }

    public String getDietDiaryName() {
        return this.dietDiaryName;
    }

    public void setDietDiaryName(String dietDiaryName) {
        this.dietDiaryName = dietDiaryName;
    }

    public List<Meal> getMeals() {
        return this.meals;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        DietDiary other = (DietDiary) obj;
        if ((this.meals != null) && (other.getMeals() != null)) {
            if (!this.meals.equals(other.getMeals())) {
                return false;
            }
        }
        return this.dietDiaryName.equals(other.getDietDiaryName());
    }
}
