package com.fitastyclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.CalorieInfo;

import java.util.Objects;

public class AddMealActivity extends MyAppCompatActivity {

    public static String addMealTitle = "Add Meal ";

    private CalorieInfo currentRecommendedCalorieInfo;
    private CalorieInfo currentMealCalorieInfo;

    protected View.OnClickListener itemsTableAddButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = getIntentWithBooleanFlag(AddMealActivity.this,
                    SearchItemsActivity.class, Utils.IS_ADD_MEAL, true);
            intent.putExtra(Utils.CALORIE_INFO, currentRecommendedCalorieInfo);
            startActivity(intent);
        }
    };

    private void updateMealItemsTable() {
        double fat = this.currentMealCalorieInfo.getFat();
        double carb = this.currentMealCalorieInfo.getCarb();
        double fiber = this.currentMealCalorieInfo.getFiber();
        double protein = this.currentMealCalorieInfo.getProtein();
        double calories = Utils.getCalories(fat, carb, protein);
        setViewWithValue(R.id.mealCaloriesValue, calories);
        setViewWithFact(R.id.mealFatValue, fat);
        setViewWithFact(R.id.mealCarbValue, carb);
        setViewWithFact(R.id.mealFiberValue, fiber);
        setViewWithFact(R.id.mealProteinValue, protein);
    }

    private void setComponents() {
        findViewById(R.id.addMealCancelIcon).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.mealItemsTableAddButton)
                .setOnClickListener(this.itemsTableAddButtonClick);
        int mealId = Objects.requireNonNull(getIntent().getExtras()).getInt(Utils.MEAL_ID);
        setViewText(R.id.addMealTitle, addMealTitle + mealId);
        this.currentRecommendedCalorieInfo = new CalorieInfo(100, 100, 100, 100);
        this.currentMealCalorieInfo = new CalorieInfo();
        updateMealItemsTable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_meal_layout);
        setComponents();
    }

}
