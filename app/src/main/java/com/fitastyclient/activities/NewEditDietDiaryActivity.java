package com.fitastyclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.CalorieInfo;

public class NewEditDietDiaryActivity extends MyAppCompatActivity {

    private CalorieInfo recommendedCalorieInfo;
    private CalorieInfo mealsCalorieInfo;
    private int newMealId;

    protected View.OnClickListener mealsTableAddButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(NewEditDietDiaryActivity.this, AddMealActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Utils.MEAL_ID, newMealId);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private void updateCalorieInfoTable() {
        double carbRecommended = this.recommendedCalorieInfo.getCarb();
        double fiberRecommended = this.recommendedCalorieInfo.getFiber();
        double fatRecommended = this.recommendedCalorieInfo.getFat();
        double proteinRecommended = this.recommendedCalorieInfo.getProtein();
        double caloriesRecommended = Utils.getCalories(fatRecommended, carbRecommended,
                proteinRecommended);

        double carbMeals = this.mealsCalorieInfo.getCarb();
        double fiberMeals = this.mealsCalorieInfo.getFiber();
        double fatMeals = this.mealsCalorieInfo.getFat();
        double proteinMeals = this.mealsCalorieInfo.getProtein();
        double caloriesMeals = Utils.getCalories(fatMeals, carbMeals, proteinMeals);

        double carbDifference = carbRecommended - carbMeals;
        double fiberDifference = fiberRecommended - fiberMeals;
        double fatDifference = fatRecommended - fatMeals;
        double proteinDifference = proteinRecommended - proteinMeals;
        double caloriesDifference = caloriesRecommended - caloriesMeals;

        setViewWithValue(R.id.caloriesRecommended, caloriesRecommended);
        setViewWithValue(R.id.caloriesSelectedMeals, caloriesMeals);
        setViewWithValue(R.id.caloriesDifference, caloriesDifference);

        setViewWithFact(R.id.fatRecommended, fatRecommended);
        setViewWithFact(R.id.fatSelectedMeals, fatMeals);
        setViewWithFact(R.id.fatDifference, fatDifference);

        setViewWithFact(R.id.carbRecommended, carbRecommended);
        setViewWithFact(R.id.carbSelectedMeals, carbMeals);
        setViewWithFact(R.id.carbDifference, carbDifference);

        setViewWithFact(R.id.fiberRecommended, fiberRecommended);
        setViewWithFact(R.id.fiberSelectedMeals, fiberMeals);
        setViewWithFact(R.id.fiberDifference, fiberDifference);

        setViewWithFact(R.id.proteinRecommended, proteinRecommended);
        setViewWithFact(R.id.proteinSelectedMeals, proteinMeals);
        setViewWithFact(R.id.proteinDifference, proteinDifference);
    }

    private void setComponents() {
        findViewById(R.id.newEditDietDiaryCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.mealsTableAddButton).setOnClickListener(this.mealsTableAddButtonClick);
        this.recommendedCalorieInfo = new CalorieInfo(100, 100, 100, 100);
        this.mealsCalorieInfo = new CalorieInfo();
        this.newMealId = 1;
        updateCalorieInfoTable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit_diet_diary_layout);
        setComponents();
    }

}
