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
import com.fitastyclient.http.HttpManager;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealActivity extends ItemsTableActivity {

    public static String addMealTitle = "Add Meal %d";
    public static String editMealTitle = "Edit Meal %d";
    public static String mealInfoTitle = "Meal %d Information";
    public static String addText = "Add";
    public static String editText = "Edit";
    static public String mealContentCantBeEmpty = "The meal content can't be empty.";
    static public String noMealChanges = "Can't edit meal if there are no changes.";

    private Utils.MealActivityType activityType;
    private int mealId;
    private Meal meal;
    private CalorieInfo currentRecommendedCalorieInfo;
    private CalorieInfo currentMealCalorieInfo;
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
            Intent intent = getIntentWithBooleanFlag(MealActivity.this,
                    SearchItemsActivity.class, Utils.IS_FOR_MEAL, true);
            intent.putExtra(Utils.CALORIE_INFO, currentRecommendedCalorieInfo);
            startActivity(intent);
        }
    };

    private View.OnClickListener addEditButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearItemsTableText();
            if (!isContentEmpty()) {
                if (activityType == Utils.MealActivityType.ADD) {
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

    private void clearItemsTableText() {
        clearInformationText(R.id.mealItemsTableInfoText);
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
            displayError(R.id.mealItemsTableInfoText, noMealChanges);
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

    private <T, S> void sendItemAddCalorieInfoRequest(Call<T> call, final S shortItem,
                                                   final boolean isIngredient,
                                                   final double amount) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call,
                                   @NonNull Response<T> response) {
                if (response.isSuccessful()) {
                    T item = response.body();
                    assert item != null;
                    CalorieInfo calorieInfo = (isIngredient)
                            ? calcIngredientCalorieInfo((Ingredient) item, amount)
                            : calcDishCalorieInfo((Dish) item, amount);
                    currentMealCalorieInfo.addOtherCalorieInfo(calorieInfo);
                    updateMealCalorieInfo();
                    clearItemsTableText();
                    if (isIngredient) {
                        addIngredientToContent((ShortIngredient) shortItem, true, true, false);
                    }
                    else addDishToContent((ShortDish) shortItem, true, false);
                } else {
                    sendItemAddFailed();
                }
            }
            @Override
            public void onFailure(@NonNull Call<T> call,
                                  @NonNull Throwable t) {
                sendItemAddFailed();
            }
        });
    }

    private void displayDeleteFailed() {
        displayActionFailed(R.id.mealItemsTableInfoText);
    }

    private void sendIngredientAddCalorieInfo(String ingredientName, ShortIngredient ingredient,
                                              double amount) {
        sendItemAddCalorieInfoRequest(HttpManager.getRetrofitApi()
                .getIngredientInfo(ingredientName), ingredient, true, amount);
    }

    private void sendDishAddCalorieInfo(String dishName, ShortDish dish, double amount) {
        sendItemAddCalorieInfoRequest(HttpManager.getRetrofitApi()
                .getDishInfo(dishName), dish, false, amount);
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
        clearItemsTableText();
        if (isIngredient) sendIngredientSubtractCalorieInfo(itemName, amount, view);
        else sendDishSubtractCalorieInfo(itemName, amount, view);
    }

    private <T> void sendItemSubtractCalorieInfoRequest(Call<T> call, final boolean isIngredient,
                                                        final double amount, final View view) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call,
                                   @NonNull Response<T> response) {
                if (response.isSuccessful()) {
                    T item = response.body();
                    assert item != null;
                    String itemName = (isIngredient) ?
                            ((Ingredient) item).getIngredientName()
                            : ((Dish) item).getDishName();
                    CalorieInfo calorieInfo = (isIngredient)
                            ? calcIngredientCalorieInfo((Ingredient) item, amount)
                            : calcDishCalorieInfo((Dish) item, amount);
                    currentMealCalorieInfo.subtractOtherCalorieInfo(calorieInfo);
                    updateMealCalorieInfo();
                    deleteItemFromDataStructures(itemName, isIngredient);
                    deleteItemFromTable(view);
                } else {
                    displayDeleteFailed();
                }
            }
            @Override
            public void onFailure(@NonNull Call<T> call,
                                  @NonNull Throwable t) {
                displayDeleteFailed();
            }
        });
    }

    private void sendIngredientSubtractCalorieInfo(String ingredientName,
                                                   double amount, View view) {
        sendItemSubtractCalorieInfoRequest(HttpManager.getRetrofitApi()
                .getIngredientInfo(ingredientName), true, amount, view);
    }

    private void sendDishSubtractCalorieInfo(String dishName, double amount, View view) {
        sendItemSubtractCalorieInfoRequest(HttpManager.getRetrofitApi()
                .getDishInfo(dishName), false, amount, view);
    }

    private void updateMealCalorieInfo() {
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

    private void populateFieldsFromMeal(boolean disableDeleteButton) {
        this.currentMealCalorieInfo = this.meal.getCalorieInfoFromFields();
        this.calorieInfoBeforeEdit = this.meal.getCalorieInfoFromFields();
        updateMealCalorieInfo();
        List<ShortDish> dishes = this.meal.getDishes();
        List<ShortIngredient> ingredients = this.meal.getIngredients();
        for (ShortDish dish : dishes) {
            addDishToContent(dish, false, disableDeleteButton);
        }
        for (ShortIngredient ingredient : ingredients) {
            addIngredientToContent(ingredient, true, false, disableDeleteButton);
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
        this.activityType = (Utils.MealActivityType)
                getIntent().getSerializableExtra(Utils.MEAL_ACTIVITY_TYPE);
        this.mealId = Objects.requireNonNull(getIntent().getExtras()).getInt(Utils.MEAL_ID);
        this.currentRecommendedCalorieInfo = (CalorieInfo)
                getIntent().getSerializableExtra(Utils.CALORIE_INFO);
        String title = Utils.EMPTY;
        String buttonText = addText;
        boolean disableDeleteButton = false;
        switch (this.activityType) {
            case ADD:
                title = addMealTitle;
                break;
            case EDIT:
                title = editMealTitle;
                buttonText = editText;
                break;
            case INFO:
                title = mealInfoTitle;
                makeButtonsInvisible();
                disableDeleteButton = true;
                break;
        }
        setViewText(R.id.mealActivityTitle, String.format(title, this.mealId));
        setViewText(R.id.addEditMealButton, buttonText);
        if (this.activityType == Utils.MealActivityType.ADD) {
            this.meal = null;
            this.currentMealCalorieInfo = new CalorieInfo();
            updateMealCalorieInfo();
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
