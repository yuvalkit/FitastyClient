package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class DietDiariesObj implements Serializable {

    @SerializedName("diet_diaries")
    private List<DietDiary> dietDiaries;

    public DietDiariesObj(List<DietDiary> dietDiaries) {
        this.dietDiaries = dietDiaries;
    }

    public List<DietDiary> getDietDiaries() {
        return this.dietDiaries;
    }
}
