package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.CalorieInfo;
import com.fitastyclient.data_holders.Meal;
import com.fitastyclient.data_holders.ShortDish;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewEditDietDiaryActivity extends MyAppCompatActivity {

    public static String mealText = "Meal ";
    public static String caloriesText = "Calories: ";

    public static int emptyWidth1 = 30;
    public static int emptyWidth2 = 20;
    public static int mealTextWidth = 100;
    public static int mealTextHeight = 150;
    public static int mealTextWeight = 1;
    public static int caloriesTextWeight = 2;
    public static int mealTextSize = 18;
    public static int bigLeftPadding = 20;
    public static int smallLeftPadding = 10;
    public static int bigIconSize = 150;
    public static int smallIconSize = 110;

    private List<Meal> meals;
    private TableLayout table;
    private CalorieInfo recommendedCalorieInfo;
    private CalorieInfo mealsCalorieInfo;
    private CalorieInfo differenceCalorieInfo;
    private int newMealId;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(Utils.ADD_MEAL_TO_DIET_DIARY)) {
                Meal meal = (Meal) intent.getSerializableExtra(Utils.MEAL);
                addMealToContent(meal);
            } else if (action.equals(Utils.EDIT_DIET_DIARY_MEAL)) {
                Meal meal = (Meal) intent.getSerializableExtra(Utils.MEAL);
                CalorieInfo calorieInfoBeforeEdit = (CalorieInfo)
                        intent.getSerializableExtra(Utils.CALORIE_INFO);
                editMeal(meal, calorieInfoBeforeEdit);
            }
        }
    };

    private View.OnClickListener mealsTableAddButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            startMealActivity(newMealId, Utils.MealActivityType.ADD, null);
        }
    };

    private void startMealActivity(int mealId, Utils.MealActivityType activityType, Meal meal) {
        Intent intent = new Intent(NewEditDietDiaryActivity.this, MealActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.MEAL_ID, mealId);
        intent.putExtras(bundle);
        intent.putExtra(Utils.MEAL_ACTIVITY_TYPE, activityType);
        intent.putExtra(Utils.CALORIE_INFO, differenceCalorieInfo);
        if ((activityType == Utils.MealActivityType.EDIT)
                || (activityType == Utils.MealActivityType.INFO)) {
            intent.putExtra(Utils.MEAL, meal);
        }
        startActivity(intent);
    }

    private void addMealToContent(Meal meal) {
        this.meals.add(meal);
        this.mealsCalorieInfo.addOtherCalorieInfo(meal.getCalorieInfoFromFields());
        updateCalorieInfoTable();
        addMealToTable(meal);
        this.newMealId++;
    }

    private void editMeal(Meal meal, CalorieInfo calorieInfoBeforeEdit) {
        calorieInfoBeforeEdit.subtractOtherCalorieInfo(meal.getCalorieInfoFromFields());
        this.mealsCalorieInfo.subtractOtherCalorieInfo(calorieInfoBeforeEdit);
        updateCalorieInfoTable();
        replaceMeal(meal);
        refreshTable();
    }

    private void replaceMeal(Meal newMeal) {
        int mealId = newMeal.getMealId();
        for (Meal meal : this.meals) {
            if (meal.getMealId() == mealId) {
                meal.replaceByMeal(newMeal);
            }
        }
    }

    private void refreshTable() {
        this.table.removeAllViews();
        for (Meal meal : this.meals) {
            addMealToTable(meal);
        }
    }

    private void addMealToTable(Meal meal) {
        TableRow row = new TableRow(this);
        String mealStr = mealText + meal.getMealId();
        String caloriesStr = caloriesText
                + Utils.cleanDoubleToString(meal.getCaloriesFromFields());
        addTextViewToRow(row, mealStr, R.color.black, mealTextWidth, mealTextHeight,
                mealTextWeight, bigLeftPadding, 0, true, 0, 0, mealTextSize);
        addTextViewToRow(row, caloriesStr, R.color.lightBlue, mealTextWidth, mealTextHeight,
                caloriesTextWeight, bigLeftPadding, 0, true, 0, 0, 0);
        addInfoButtonToRow(row, meal);
        addEmptyTextViewToRow(row, emptyWidth1, 0, 0);
        addEditButtonToRow(row, meal);
        addEmptyTextViewToRow(row, emptyWidth2, 0, 0);
        addDeleteButtonToRow(row, meal);
        this.table.addView(row);
    }

    private void addInfoButtonToRow(TableRow row, final Meal meal) {
        ImageView view = getImageView(android.R.drawable.ic_dialog_info,
                smallIconSize, R.color.blue, smallLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMealActivity(meal.getMealId(), Utils.MealActivityType.INFO, meal);
            }
        });
        row.addView(view);
    }

    private void addEditButtonToRow(TableRow row, final Meal meal) {
        ImageView view = getImageView(android.R.drawable.ic_menu_manage,
                bigIconSize, R.color.black, smallLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMealActivity(meal.getMealId(), Utils.MealActivityType.EDIT, meal);
            }
        });
        row.addView(view);
    }

    private void addDeleteButtonToRow(TableRow row, final Meal meal) {
        ImageView view = getImageView(android.R.drawable.btn_dialog,
                bigIconSize, 0, smallLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemFromTable(v);
                deleteMealFromContent(meal);
            }
        });
        row.addView(view);
    }

    private void deleteMealFromContent(Meal meal) {
        this.mealsCalorieInfo.subtractOtherCalorieInfo(meal.getCalorieInfoFromFields());
        updateCalorieInfoTable();
        deleteMealFromList(meal.getMealId());
        this.table.removeAllViews();
        resetMealsIdsAndAddToTable();
    }

    private void deleteMealFromList(int mealId) {
        Iterator<Meal> iterator = this.meals.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getMealId() == mealId) {
                iterator.remove();
            }
        }
    }

    private void resetMealsIdsAndAddToTable() {
        int mealId = 1;
        for (Meal meal : this.meals) {
            meal.setMealId(mealId);
            addMealToTable(meal);
            mealId++;
        }
        this.newMealId = mealId;
    }

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

        this.differenceCalorieInfo.setCarb(carbDifference);
        this.differenceCalorieInfo.setFiber(fiberDifference);
        this.differenceCalorieInfo.setFat(fatDifference);
        this.differenceCalorieInfo.setProtein(proteinDifference);

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
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.ADD_MEAL_TO_DIET_DIARY));
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.EDIT_DIET_DIARY_MEAL));
        this.meals = new ArrayList<>();
        this.table = findViewById(R.id.mealsTable);
        this.recommendedCalorieInfo = new CalorieInfo(100, 100, 100, 100);
        this.mealsCalorieInfo = new CalorieInfo();
        this.differenceCalorieInfo = new CalorieInfo();
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
