package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.DishToInsert;
import com.fitastyclient.data_holders.NameExistObj;
import com.fitastyclient.data_holders.ShortDish;
import com.fitastyclient.data_holders.ShortIngredient;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDishActivity extends ItemsTableActivity {

    public static String dishContentCantBeEmpty = "The dish content can't be empty.";
    public static String nameAlreadyUsed = "There is already a dish with this name.";
    public static String dishAdded = "Dish Added";

    private String username;

    protected BroadcastReceiver itemBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(ingredientFlag)) {
                ShortIngredient ingredient = (ShortIngredient)
                        intent.getSerializableExtra(Utils.INGREDIENT);
                assert ingredient != null;
                if (checkIfItemCanBeAdded(ingredientsNameSet, ingredient.getIngredientName())) {
                    addIngredientToContent(ingredient, false, true, false,
                            R.id.addDishButtonInfoText);
                }
            } else if (action.equals(dishFlag)) {
                ShortDish dish = (ShortDish) intent.getSerializableExtra(Utils.DISH);
                assert dish != null;
                if (checkIfItemCanBeAdded(dishesNameSet, dish.getDishName())) {
                    addDishToContent(dish, true, false, R.id.addDishButtonInfoText);
                }
            }
        }
    };

    protected BroadcastReceiver getItemBroadcastReceiver() {
        return this.itemBroadcastReceiver;
    }

    private View.OnClickListener itemsTableAddButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            tryToStartSearchItemsActivity(username, AddDishActivity.this, false,
                    R.id.addDishButtonInfoText, null);
        }
    };

    private View.OnClickListener addButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearInformationText(R.id.addDishButtonInfoText);
            if (checkFields()) {
                addDish();
            }
        }
    };

    private void displayAddFailed() {
        displayActionFailed(R.id.addDishButtonInfoText);
    }

    private void displayNameAlreadyUsed() {
        displayError(R.id.dishNameInformationText, nameAlreadyUsed);
    }

    protected void deleteItemFromContent(View view, String itemName, boolean isIngredient,
                                         double amount) {
        deleteItemFromDataStructures(itemName, isIngredient);
        deleteItemFromTable(view);
    }

    private boolean checkFields() {
        boolean valid = true;
        if (isFieldEmptyQuery(R.id.dishNameInput, R.id.dishNameInformationText)) {
            valid = false;
        }
        if (this.ingredients.isEmpty() && this.dishes.isEmpty()) {
            displayError(R.id.addDishButtonInfoText, dishContentCantBeEmpty);
            valid = false;
        } else {
            clearInformationText(R.id.addDishButtonInfoText);
        }
        return valid;
    }

    private void addDish() {
        String dishName = getTextFromView(R.id.dishNameInput);
        DishToInsert dishToInsert = new DishToInsert(dishName, this.ingredients, this.dishes);
        Utils.getRetrofitApi().insertNewDish(dishToInsert)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            NameExistObj nameExistObj = Utils.getResponseNameExistObj(response);
                            if (nameExistObj != null) {
                                if (nameExistObj.getNameExist()) {
                                    displayNameAlreadyUsed();
                                } else {
                                    Utils.displayToast(getApplicationContext(), dishAdded);
                                    finish();
                                }
                            } else {
                                displayAddFailed();
                            }
                        } else {
                            displayAddFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayAddFailed();
                    }
                });
    }

    private void setTableWidth() {
        int screenWidth = getScreenWidth();
        int tablesWidths = (int) (screenWidth * tableWidthPercent);
        setViewWidth(R.id.itemsTableScrollView, tablesWidths);
        setViewWidth(R.id.itemsTable, tablesWidths);
    }

    private void setComponents() {
        this.username = Objects.requireNonNull(getIntent().getExtras()).getString(Utils.USERNAME);
        this.typeFlag = Utils.DISH_FLAG;
        findViewById(R.id.addDishCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.addNewDishButton).setOnClickListener(this.addButtonClick);
        findViewById(R.id.itemsTableAddButton).setOnClickListener(this.itemsTableAddButtonClick);
        setItemsTableComponents(R.id.itemsTable);
        setTableWidth();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dish_layout);
        setComponents();
    }
}
