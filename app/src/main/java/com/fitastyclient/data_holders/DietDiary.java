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

}
