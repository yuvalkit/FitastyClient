package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import androidx.annotation.NonNull;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.CalorieInfo;
import com.fitastyclient.data_holders.DietDiary;
import com.fitastyclient.data_holders.Meal;
import com.fitastyclient.data_holders.NameExistObj;
import com.fitastyclient.data_holders.ShortIngredient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietDiaryActivity extends MyAppCompatActivity {

    public static String mealText = "Meal ";
    public static String caloriesText = "Calories: ";
    public static String atLeastOneMeal = "You must add at least one meal.";
    public static String dietDiaryCreationFailed = "Diet diary creation failed, please try again.";
    public static String dietDiaryEditFailed = "Diet diary edit failed, please try again.";
    public static String dietDiaryNameAlreadyUsed = "You already have a diet diary with this name.";
    public static String dietDiaryCreated = "Diet Diary Created";
    public static String dietDiaryEdited = "Diet Diary Edited";
    public static String createDietDiaryTitle = "Create New Diet Diary";
    public static String editDietDiaryTitle = "Edit Diet Diary";
    public static String dietDiaryInfoTitle = "Diet Diary Information";
    public static String noDietDiaryChanges = "Can't edit diet diary if there are no changes.";
    public static String editCreatePopupTitle = "Values are not recommended";
    public static String editCreatePopupTextFormat =
            "This diet diary calorie information difference is not recommended for you\n" +
                    "(there are red values).\n\n" +
                    "Do you want to %s this diet diary anyway?";

    public static int emptyWidth1 = 30;
    public static int emptyWidth2 = 20;
    public static int mealTextWidth = 100;
    public static int mealTextHeight = 250;
    public static int mealTextWeight = 1;
    public static int caloriesTextWeight = 2;
    public static int mealTextSize = 18;
    public static int bigLeftPadding = 20;
    public static int smallLeftPadding = 10;
    public static int bigIconSize = 150;
    public static int smallIconSize = 110;

    private String username;
    private DietDiary dietDiary;
    private Utils.ActivityType activityType;
    private List<Meal> meals;
    private TableLayout table;
    private CalorieInfo recommendedCalorieInfo;
    private CalorieInfo mealsCalorieInfo;
    private CalorieInfo differenceCalorieInfo;
    private int newMealId;
    private boolean isDifferenceRecommended;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(Utils.ADD_MEAL_TO_DIET_DIARY)) {
                Meal meal = (Meal) intent.getSerializableExtra(Utils.MEAL);
                addMealToContent(meal, false);
            } else if (action.equals(Utils.EDIT_DIET_DIARY_MEAL)) {
                Meal meal = (Meal) intent.getSerializableExtra(Utils.MEAL);
                CalorieInfo calorieInfoBeforeEdit = (CalorieInfo)
                        intent.getSerializableExtra(Utils.CALORIE_INFO);
                assert meal != null;
                assert calorieInfoBeforeEdit != null;
                editMeal(meal, calorieInfoBeforeEdit);
            }
        }
    };

    private View.OnClickListener mealsTableAddButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            startMealActivity(newMealId, Utils.ActivityType.CREATE, null);
        }
    };

    private View.OnClickListener createEditButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearInfoTexts();
            if (checkFields()) {
                DietDiary newDietDiary = getDietDiaryFromFields();
                boolean isCreateDietDiary = (activityType == Utils.ActivityType.CREATE);
                if (!isCreateDietDiary && newDietDiary.equals(dietDiary)) {
                    displayDietDiaryError(noDietDiaryChanges);
                } else {
                    dietDiaryCreateEditOrPopup(newDietDiary, isCreateDietDiary);
                }
            }
        }
    };

    private void dietDiaryCreateEditOrPopup(DietDiary newDietDiary, boolean isCreateDietDiary) {
        if (this.isDifferenceRecommended) createOrEditDietDiary(newDietDiary, isCreateDietDiary);
        else displayEditCreatePopup(newDietDiary, isCreateDietDiary);
    }

    private void displayEditCreatePopup(final DietDiary newDietDiary,
                                        final boolean isCreateDietDiary) {
        String activityTypeText = (isCreateDietDiary) ?
                createText.toLowerCase() : editText.toLowerCase();
        String editCreatePopupText = String.format(editCreatePopupTextFormat, activityTypeText);
        displayAlertPopup(editCreatePopupTitle, editCreatePopupText, R.color.mildGray,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        createOrEditDietDiary(newDietDiary, isCreateDietDiary);
                    }
        });
    }

    private void editDietDiary(final DietDiary newDietDiary) {
        Utils.getRetrofitApi().updateDietDiary(this.username,
                this.dietDiary.getDietDiaryName(), newDietDiary)
                .enqueue(getDietDiaryCallback(newDietDiary.getDietDiaryName(),
                        Utils.EDIT_DIET_DIARY, dietDiaryEdited, dietDiaryEditFailed));
    }

    private void createDietDiary(final DietDiary newDietDiary) {
        Utils.getRetrofitApi().insertDietDiary(this.username, newDietDiary)
                .enqueue(getDietDiaryCallback(newDietDiary.getDietDiaryName(),
                Utils.ADD_DIET_DIARY, dietDiaryCreated, dietDiaryCreationFailed));
    }

    private void createOrEditDietDiary(DietDiary dietDiary, boolean isCreateDietDiary) {
        if (isCreateDietDiary) createDietDiary(dietDiary);
        else editDietDiary(dietDiary);
    }

    private void displayDietDiaryError(String errorText) {
        displayError(R.id.createEditDietDiaryInfoText, errorText);
    }

    private void displayDietDiaryNameAlreadyUsed() {
        displayError(R.id.dietDiaryNameInfoText, dietDiaryNameAlreadyUsed);
    }

    private DietDiary getDietDiaryFromFields() {
        String dietDiaryName = getTextFromView(R.id.dietDiaryNameInput);
        List<Meal> shortMeals = new ArrayList<>();
        for (Meal meal : this.meals) {
            List<ShortIngredient> ingredients = meal.getIngredients();
            List<ShortIngredient> shortIngredients = new ArrayList<>();
            for (ShortIngredient ingredient : ingredients) {
                shortIngredients.add(new ShortIngredient(
                        ingredient.getIngredientName(), ingredient.getAmount()));
            }
            shortMeals.add(new Meal(meal.getMealId(), meal.getDishes(), shortIngredients));
        }
        return new DietDiary(dietDiaryName, shortMeals);
    }

    private Callback<ResponseBody> getDietDiaryCallback(final String dietDiaryName,
                                                        final String intentFlag,
                                                        final String toastText,
                                                        final String errorText) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    NameExistObj nameExistObj = Utils.getResponseNameExistObj(response);
                    if (nameExistObj != null) {
                        if (nameExistObj.getNameExist()) {
                            displayDietDiaryNameAlreadyUsed();
                        } else {
                            Intent intent = getDietDiaryIntent(
                                    new DietDiary(dietDiaryName), intentFlag);
                            if (intentFlag.equals(Utils.EDIT_DIET_DIARY)) {
                                Bundle bundle = new Bundle();
                                bundle.putString(Utils.PREV_DIET_DIARY_NAME,
                                        dietDiary.getDietDiaryName());
                                intent.putExtras(bundle);
                            }
                            sendBroadcast(intent);
                            Utils.displayToast(getApplicationContext(), toastText);
                            finish();
                        }
                    } else {
                        displayDietDiaryError(errorText);
                    }
                } else {
                    displayDietDiaryError(errorText);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                displayDietDiaryError(errorText);
            }
        };
    }

    private Intent getDietDiaryIntent(DietDiary dietDiary, String intentFlag) {
        Intent intent = new Intent(intentFlag);
        intent.putExtra(Utils.DIET_DIARY, dietDiary);
        return intent;
    }

    private boolean checkFields() {
        boolean valid = true;
        int mealsInfoText = R.id.mealsTableInfoText;
        if (isFieldEmptyQuery(R.id.dietDiaryNameInput, R.id.dietDiaryNameInfoText)) {
            valid = false;
        }
        if (this.meals.isEmpty()) {
            displayError(mealsInfoText, atLeastOneMeal);
            valid = false;
        } else {
            clearInformationText(mealsInfoText);
        }
        return valid;
    }

    private void clearInfoTexts() {
        clearInformationText(R.id.createEditDietDiaryInfoText);
        clearInformationText(R.id.mealsTableInfoText);
    }

    private void startMealActivity(int mealId, Utils.ActivityType activityType, Meal meal) {
        Intent intent = Utils.getIntentWithUsername(DietDiaryActivity.this,
                MealActivity.class, username);
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.MEAL_ID, mealId);
        intent.putExtras(bundle);
        intent.putExtra(Utils.MEAL_ACTIVITY_TYPE, activityType);
        if (activityType == Utils.ActivityType.CREATE) {
            intent.putExtra(Utils.CALORIE_INFO, differenceCalorieInfo);
        } else {
            CalorieInfo differenceWithoutCurrentMeal = new CalorieInfo();
            differenceWithoutCurrentMeal.addOtherCalorieInfo(differenceCalorieInfo);
            differenceWithoutCurrentMeal.addOtherCalorieInfo(meal.getCalorieInfoFromFields());
            intent.putExtra(Utils.CALORIE_INFO, differenceWithoutCurrentMeal);
            intent.putExtra(Utils.MEAL, meal);
        }
        startActivity(intent);
    }

    private void addMealToContent(Meal meal, boolean disableEditButtons) {
        this.meals.add(meal);
        this.mealsCalorieInfo.addOtherCalorieInfo(meal.getCalorieInfoFromFields());
        updateDietDiaryCalorieInfoTable();
        addMealToTable(meal, disableEditButtons);
        this.newMealId++;
    }

    private void editMeal(Meal meal, CalorieInfo calorieInfoBeforeEdit) {
        calorieInfoBeforeEdit.subtractOtherCalorieInfo(meal.getCalorieInfoFromFields());
        this.mealsCalorieInfo.subtractOtherCalorieInfo(calorieInfoBeforeEdit);
        updateDietDiaryCalorieInfoTable();
        replaceMeal(meal);
        refreshTable();
    }

    private void replaceMeal(Meal newMeal) {
        int mealId = newMeal.getMealId();
        for (Meal meal : this.meals) {
            if (meal.getMealId() == mealId) {
                int mealIndex = this.meals.indexOf(meal);
                this.meals.set(mealIndex, newMeal);
            }
        }
    }

    private void refreshTable() {
        this.table.removeAllViews();
        for (Meal meal : this.meals) {
            addMealToTable(meal, false);
        }
    }

    private void addMealToTable(Meal meal, boolean disableEditButtons) {
        TableRow row = new TableRow(this);
        String mealStr = mealText + meal.getMealId();
        String caloriesStr = caloriesText
                + Utils.cleanDoubleToString(meal.getCaloriesFromFields());
        addTextViewToRow(row, mealStr, R.color.black, mealTextWidth, mealTextHeight,
                mealTextWeight, bigLeftPadding, true, 0, 0, mealTextSize);
        addTextViewToRow(row, caloriesStr, R.color.lightBlue, mealTextWidth, mealTextHeight,
                caloriesTextWeight, bigLeftPadding, true, 0, 0, 0);
        addInfoButtonToRow(row, meal);
        addEmptyTextViewToRow(row, emptyWidth1);
        if (!disableEditButtons) {
            addEditButtonToRow(row, meal);
            addEmptyTextViewToRow(row, emptyWidth2);
            addDeleteButtonToRow(row, meal);
        }
        this.table.addView(row);
    }

    private void addInfoButtonToRow(TableRow row, final Meal meal) {
        ImageView view = getImageView(android.R.drawable.ic_dialog_info,
                smallIconSize, R.color.blue, smallLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInfoTexts();
                startMealActivity(meal.getMealId(), Utils.ActivityType.INFO, meal);
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
                clearInfoTexts();
                startMealActivity(meal.getMealId(), Utils.ActivityType.EDIT, meal);
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
                clearInfoTexts();
                deleteItemFromTable(v);
                deleteMealFromContent(meal);
            }
        });
        row.addView(view);
    }

    private void deleteMealFromContent(Meal meal) {
        this.mealsCalorieInfo.subtractOtherCalorieInfo(meal.getCalorieInfoFromFields());
        updateDietDiaryCalorieInfoTable();
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
            addMealToTable(meal, false);
            mealId++;
        }
        this.newMealId = mealId;
    }

    private void updateDietDiaryCalorieInfoTable() {
        updateCalorieInfoTable(this.recommendedCalorieInfo, this.mealsCalorieInfo,
                this.differenceCalorieInfo,
                R.id.caloriesRecommended, R.id.caloriesSelectedMeals, R.id.caloriesDifference,
                R.id.fatRecommended, R.id.fatSelectedMeals, R.id.fatDifference,
                R.id.carbRecommended, R.id.carbSelectedMeals, R.id.carbDifference,
                R.id.fiberRecommended, R.id.fiberSelectedMeals, R.id.fiberDifference,
                R.id.proteinRecommended, R.id.proteinSelectedMeals, R.id.proteinDifference);
        this.isDifferenceRecommended = getIsDifferenceRecommended();
    }

    private boolean getIsDifferenceRecommended() {
        boolean isRecommended = true;
        double fatRecommended = this.recommendedCalorieInfo.getFat();
        double carbRecommended = this.recommendedCalorieInfo.getCarb();
        double fiberRecommended = this.recommendedCalorieInfo.getFiber();
        double proteinRecommended = this.recommendedCalorieInfo.getProtein();
        double fatDifference = this.differenceCalorieInfo.getFat();
        double carbDifference = this.differenceCalorieInfo.getCarb();
        double fiberDifference = this.differenceCalorieInfo.getFiber();
        double proteinDifference = this.differenceCalorieInfo.getProtein();
        if (isValueOutOfThresholdRange(fatRecommended, fatDifference)
                || isValueOutOfThresholdRange(carbRecommended, carbDifference)
                || isValueOutOfThresholdRange(fiberRecommended, fiberDifference)
                || isValueOutOfThresholdRange(proteinRecommended, proteinDifference)) {
            isRecommended = false;
        } else {
            double caloriesRecommended = Utils.getCalories(fatRecommended, carbRecommended,
                    proteinRecommended);
            double caloriesDifference = Utils.getCalories(fatDifference, carbDifference,
                    proteinDifference);
            if (isValueOutOfThresholdRange(caloriesRecommended, caloriesDifference)) {
                isRecommended = false;
            }
        }
        return isRecommended;
    }

    private boolean isValueOutOfThresholdRange(double baseValue, double value) {
        double threshold = baseValue * Utils.GRAY_THRESHOLD_PERCENT;
        return !Utils.isValueInThresholdRange(value, threshold);
    }

    private void setToInfoActivity() {
        findViewById(R.id.dietDiaryNameInput).setFocusable(false);
        makeViewInvisible(R.id.mealsTableAddButton);
        makeViewInvisible(R.id.dietDiaryActivityButton);
    }

    private void setTablesWidths() {
        int screenWidth = getScreenWidth();
        int tablesWidths = (int) (screenWidth * tableWidthPercent);
        setViewWidth(R.id.dietDiaryMealsTableScrollView, tablesWidths);
        setViewWidth(R.id.dietDiaryMealsTable, tablesWidths);
        setViewWidth(R.id.dietDiaryMealsTableScrollView, tablesWidths);
        setViewWidth(R.id.calorieInfoTableHorizontalScrollView, tablesWidths);
    }

    private void setComponents() {
        findViewById(R.id.dietDiaryActivityCancelIcon).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.mealsTableAddButton).setOnClickListener(this.mealsTableAddButtonClick);
        findViewById(R.id.dietDiaryActivityButton).setOnClickListener(this.createEditButtonClick);
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.ADD_MEAL_TO_DIET_DIARY));
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.EDIT_DIET_DIARY_MEAL));
        setTablesWidths();
        this.username = Objects.requireNonNull(getIntent().getExtras()).getString(Utils.USERNAME);
        this.table = findViewById(R.id.dietDiaryMealsTable);
        this.activityType = (Utils.ActivityType)
                getIntent().getSerializableExtra(Utils.DIET_DIARY_ACTIVITY_TYPE);
        String title = createDietDiaryTitle;
        String buttonText = createText;
        boolean disableEditButtons = false;
        if (activityType == Utils.ActivityType.EDIT) {
            title = editDietDiaryTitle;
            buttonText = editText;
        } else if (activityType == Utils.ActivityType.INFO) {
            title = dietDiaryInfoTitle;
            setToInfoActivity();
            disableEditButtons = true;
        }
        setViewText(R.id.dietDiaryActivityTitle, title);
        setViewText(R.id.dietDiaryActivityButton, buttonText);
        this.recommendedCalorieInfo = (CalorieInfo)
                getIntent().getSerializableExtra(Utils.CALORIE_INFO);
        this.meals = new ArrayList<>();
        this.mealsCalorieInfo = new CalorieInfo();
        this.differenceCalorieInfo = new CalorieInfo();
        this.newMealId = 1;
        if (this.activityType == Utils.ActivityType.CREATE) {
            updateDietDiaryCalorieInfoTable();
        } else {
            this.dietDiary = (DietDiary) getIntent().getSerializableExtra(Utils.DIET_DIARY);
            assert this.dietDiary != null;
            setViewText(R.id.dietDiaryNameInput, this.dietDiary.getDietDiaryName());
            List<Meal> dietDiaryMeals = this.dietDiary.getMeals();
            for (Meal meal : dietDiaryMeals) {
                addMealToContent(meal, disableEditButtons);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_diary_layout);
        setComponents();
    }

}
