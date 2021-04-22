package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.CalorieInfo;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.data_holders.Meal;
import com.fitastyclient.data_holders.ShortDish;
import com.fitastyclient.data_holders.ShortIngredient;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealActivity extends ItemsTableActivity {

    public static String addMealTitle = "Add Meal %d";
    public static String editMealTitle = "Edit Meal %d";
    public static String mealInfoTitle = "Meal %d Information";
    static public String mealContentCantBeEmpty = "The meal content can't be empty.";
    static public String noMealChanges = "Can't edit meal if there are no changes.";

    private Utils.ActivityType activityType;
    private int mealId;
    private Meal meal;
    private CalorieInfo recommendedCalorieInfo;
    private CalorieInfo currentMealCalorieInfo;
    private CalorieInfo differenceCalorieInfo;
    private CalorieInfo calorieInfoBeforeEdit;

    protected BroadcastReceiver itemBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(ingredientFlag)) {
                ShortIngredient ingredient = (ShortIngredient)
                        intent.getSerializableExtra(Utils.INGREDIENT);
                assert ingredient != null;
                String ingredientName = ingredient.getIngredientName();
                if (checkIfItemCanBeAdded(ingredientsNameSet, ingredientName)) {
                    sendIngredientAddCalorieInfo(ingredientName, ingredient,
                            ingredient.getAmount());
                }
            } else if (action.equals(dishFlag)) {
                ShortDish dish = (ShortDish) intent.getSerializableExtra(Utils.DISH);
                assert dish != null;
                String dishName = dish.getDishName();
                if (checkIfItemCanBeAdded(dishesNameSet,dishName )) {
                    sendDishAddCalorieInfo(dishName, dish, dish.getPercent());
                }
            }
        }
    };

    protected BroadcastReceiver getItemBroadcastReceiver() {
        return this.itemBroadcastReceiver;
    }

    private View.OnClickListener itemsTableAddButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearInfoTexts();
            Intent intent = getIntentWithBooleanFlag(MealActivity.this,
                    SearchItemsActivity.class, Utils.IS_FOR_MEAL, true);
            intent.putExtra(Utils.CALORIE_INFO, differenceCalorieInfo);
            startActivity(intent);
        }
    };

    private View.OnClickListener addEditButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearInfoTexts();
            if (!isContentEmpty()) {
                if (activityType == Utils.ActivityType.CREATE) {
                    addMeal();
                } else {
                    editMeal();
                }
            }
        }
    };

    private boolean isContentEmpty() {
        if (this.ingredients.isEmpty() && this.dishes.isEmpty()) {
            displayError(R.id.mealItemsTableInfoText, mealContentCantBeEmpty);
            return true;
        }
        return false;
    }

    private void clearInfoTexts() {
        clearInformationText(R.id.mealItemsTableInfoText);
        clearInformationText(R.id.mealButtonInfoText);
    }

    private void addMeal() {
        Meal meal = getMealFromFields();
        Intent intent = getMealIntent(meal, Utils.ADD_MEAL_TO_DIET_DIARY);
        sendBroadcast(intent);
        finish();
    }

    private void editMeal() {
        Meal meal = getMealFromFields();
        if (meal.equals(this.meal)) {
            displayError(R.id.mealButtonInfoText, noMealChanges);
            return;
        }
        Intent intent = getMealIntent(meal, Utils.EDIT_DIET_DIARY_MEAL);
        intent.putExtra(Utils.CALORIE_INFO, this.calorieInfoBeforeEdit);
        sendBroadcast(intent);
        finish();
    }

    private Intent getMealIntent(Meal meal, String intentFlag) {
        Intent intent = new Intent(intentFlag);
        intent.putExtra(Utils.MEAL, meal);
        return intent;
    }

    private Meal getMealFromFields() {
        Double fat = this.currentMealCalorieInfo.getFat();
        Double carb = this.currentMealCalorieInfo.getCarb();
        Double fiber = this.currentMealCalorieInfo.getFiber();
        Double protein = this.currentMealCalorieInfo.getProtein();
        return new Meal(this.mealId, this.dishes, this.ingredients, fat, carb, fiber, protein);
    }

    private void sendItemCalorieInfoToSearchItems(CalorieInfo calorieInfo) {
        Intent intent = new Intent(Utils.UPDATE_FACTS_FILTER_BY_ITEM);
        intent.putExtra(Utils.CALORIE_INFO, calorieInfo);
        sendBroadcast(intent);
    }

    private <T, S> void sendItemAddCalorieInfoRequest(Call<ResponseBody> call, final S shortItem,
                                                      final boolean isIngredient,
                                                      final double amount,
                                                      final Class<T> itemClass) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    T item = Utils.getResponseObject(response, itemClass);
                    if (item != null) {
                        CalorieInfo calorieInfo = (isIngredient)
                                ? calcIngredientCalorieInfo((Ingredient) item, amount)
                                : calcDishCalorieInfo((Dish) item, amount);
                        currentMealCalorieInfo.addOtherCalorieInfo(calorieInfo);
                        updateMealCalorieInfoTable();
                        sendItemCalorieInfoToSearchItems(calorieInfo);
                        clearInfoTexts();
                        if (isIngredient) {
                            addIngredientToContent((ShortIngredient) shortItem, true, true, false,
                                    R.id.mealButtonInfoText);
                        }
                        else addDishToContent((ShortDish) shortItem, true, false,
                                R.id.mealButtonInfoText);
                    } else {
                        sendItemAddFailed();
                    }
                } else {
                    sendItemAddFailed();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                sendItemAddFailed();
            }
        });
    }

    private void displayDeleteFailed() {
        displayActionFailed(R.id.mealButtonInfoText);
    }

    private void sendIngredientAddCalorieInfo(String ingredientName, ShortIngredient ingredient,
                                              double amount) {
        sendItemAddCalorieInfoRequest(Utils.getRetrofitApi().getIngredientInfo(ingredientName),
                ingredient, true, amount, Ingredient.class);
    }

    private void sendDishAddCalorieInfo(String dishName, ShortDish dish, double amount) {
        sendItemAddCalorieInfoRequest(Utils.getRetrofitApi().getDishInfo(dishName),
                dish, false, amount, Dish.class);
    }

    private CalorieInfo calcIngredientCalorieInfo(Ingredient ingredient, double amount) {
        double carb = calcIngredientValue(ingredient.getCarb(), amount);
        double fiber = calcIngredientValue(ingredient.getFiber(), amount);
        double fat = calcIngredientValue(ingredient.getFat(), amount);
        double protein = calcIngredientValue(ingredient.getProtein(), amount);
        return new CalorieInfo(carb, fiber, fat, protein);
    }

    private CalorieInfo calcDishCalorieInfo(Dish dish, double amount) {
        double carb = calcDishValue(dish.getCarb(), amount);
        double fiber = calcDishValue(dish.getFiber(), amount);
        double fat = calcDishValue(dish.getFat(), amount);
        double protein = calcDishValue(dish.getProtein(), amount);
        return new CalorieInfo(carb, fiber, fat, protein);
    }

    private double calcIngredientValue(double value, double amount) {
        return ((value / 100) * amount);
    }

    private double calcDishValue(double value, double amount) {
        return (value * amount);
    }

    private void sendItemAddFailed() {
        sendBroadcast(new Intent(Utils.ITEM_ADD_FAILED));
    }

    protected void deleteItemFromContent(View view, String itemName, boolean isIngredient,
                                         double amount) {
        clearInfoTexts();
        if (isIngredient) sendIngredientSubtractCalorieInfo(itemName, amount, view);
        else sendDishSubtractCalorieInfo(itemName, amount, view);
    }

    private <T> void sendItemSubtractCalorieInfoRequest(Call<ResponseBody> call, final boolean isIngredient,
                                                        final double amount, final View view,
                                                        final Class<T> itemClass) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    T item = Utils.getResponseObject(response, itemClass);
                    if (item != null) {
                        String itemName = (isIngredient) ?
                                ((Ingredient) item).getIngredientName()
                                : ((Dish) item).getDishName();
                        CalorieInfo calorieInfo = (isIngredient)
                                ? calcIngredientCalorieInfo((Ingredient) item, amount)
                                : calcDishCalorieInfo((Dish) item, amount);
                        currentMealCalorieInfo.subtractOtherCalorieInfo(calorieInfo);
                        updateMealCalorieInfoTable();
                        deleteItemFromDataStructures(itemName, isIngredient);
                        deleteItemFromTable(view);
                    } else {
                        displayDeleteFailed();
                    }
                } else {
                    displayDeleteFailed();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                displayDeleteFailed();
            }
        });
    }

    private void sendIngredientSubtractCalorieInfo(String ingredientName,
                                                   double amount, View view) {
        sendItemSubtractCalorieInfoRequest(Utils.getRetrofitApi()
                .getIngredientInfo(ingredientName), true, amount, view, Ingredient.class);
    }

    private void sendDishSubtractCalorieInfo(String dishName, double amount, View view) {
        sendItemSubtractCalorieInfoRequest(Utils.getRetrofitApi()
                .getDishInfo(dishName), false, amount, view, Dish.class);
    }

    private void updateMealCalorieInfoTable() {
        updateCalorieInfoTable(this.recommendedCalorieInfo, this.currentMealCalorieInfo,
                this.differenceCalorieInfo,
                R.id.mealRecommendedCalories, R.id.thisMealCalories, R.id.mealCaloriesDifference,
                R.id.mealRecommendedFat, R.id.thisMealFat, R.id.mealFatDifference,
                R.id.mealRecommendedCarb, R.id.thisMealCarb, R.id.mealCarbDifference,
                R.id.mealRecommendedFiber, R.id.thisMealFiber, R.id.mealFiberDifference,
                R.id.mealRecommendedProtein, R.id.thisMealProtein, R.id.mealProteinDifference);
    }

    private void populateFieldsFromMeal(boolean disableDeleteButton) {
        this.currentMealCalorieInfo = this.meal.getCalorieInfoFromFields();
        this.calorieInfoBeforeEdit = this.meal.getCalorieInfoFromFields();
        updateMealCalorieInfoTable();
        List<ShortDish> dishes = this.meal.getDishes();
        List<ShortIngredient> ingredients = this.meal.getIngredients();
        for (ShortDish dish : dishes) {
            addDishToContent(dish, false, disableDeleteButton, R.id.mealButtonInfoText);
        }
        for (ShortIngredient ingredient : ingredients) {
            addIngredientToContent(ingredient, true, false, disableDeleteButton,
                    R.id.mealButtonInfoText);
        }
    }

    private void makeButtonsInvisible() {
        makeViewInvisible(R.id.mealItemsTableAddButton);
        makeViewInvisible(R.id.addEditMealButton);
    }

    private void setComponents() {
        this.typeFlag = Utils.MEAL_FLAG;
        findViewById(R.id.addMealCancelIcon).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.addEditMealButton).setOnClickListener(this.addEditButtonClick);
        findViewById(R.id.mealItemsTableAddButton)
                .setOnClickListener(this.itemsTableAddButtonClick);
        findViewById(R.id.mealItemsTableAddButton)
                .setOnClickListener(this.itemsTableAddButtonClick);
        setItemsTableComponents(R.id.mealItemsTable);
        this.activityType = (Utils.ActivityType)
                getIntent().getSerializableExtra(Utils.MEAL_ACTIVITY_TYPE);
        this.mealId = Objects.requireNonNull(getIntent().getExtras()).getInt(Utils.MEAL_ID);
        this.recommendedCalorieInfo = (CalorieInfo)
                getIntent().getSerializableExtra(Utils.CALORIE_INFO);
        this.differenceCalorieInfo = new CalorieInfo();
        String title = addMealTitle;
        String buttonText = addText;
        boolean disableDeleteButton = false;
        if (activityType == Utils.ActivityType.EDIT) {
            title = editMealTitle;
            buttonText = editText;
        } else if (activityType == Utils.ActivityType.INFO) {
            title = mealInfoTitle;
            makeButtonsInvisible();
            disableDeleteButton = true;
        }
        setViewText(R.id.mealActivityTitle, String.format(title, this.mealId));
        setViewText(R.id.addEditMealButton, buttonText);
        if (this.activityType == Utils.ActivityType.CREATE) {
            this.meal = null;
            this.currentMealCalorieInfo = new CalorieInfo();
            updateMealCalorieInfoTable();
        } else {
            this.meal = (Meal) getIntent().getSerializableExtra(Utils.MEAL);
            populateFieldsFromMeal(disableDeleteButton);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_layout);
        setComponents();
    }

}
