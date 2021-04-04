package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.DishToInsert;
import com.fitastyclient.data_holders.NameExistObject;
import com.fitastyclient.data_holders.ShortDish;
import com.fitastyclient.data_holders.ShortIngredient;
import com.fitastyclient.http.HttpManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDishActivity extends MyAppCompatActivity {

    static public String nameTitle = "Name";
    static public String amountTitle = "Amount";
    static public String typeTitle = "Type";
    static public String dishContentCantBeEmpty = "The dish content can't be empty.";
    static public String nameAlreadyUsed = "There is already a dish with this name.";
    static public String dishAdded = "Dish Added";

    static public int titleRowHeight = 100;
    static public int titleBackgroundColor = R.color.veryLightBlue;
    static public int itemRowHeight = 130;
    static public int typeWidth = 310;
    static public int typePadding = 20;
    static public int nameWidth = 820;
    static public int namePadding = 20;
    static public int amountWidth = 260;
    static public int amountPadding = 30;
    static public int deleteWidth = 120;
    static public int deletePadding = 10;
    static public int bigWeight = 3;
    static public int smallWeight = 1;

    static public int maxNameSizeInRow = 25;
    static public int maxAmountSizeInRow = 4;
    static public int heightGaps = 30;

    private TableLayout table;
    private Set<String> ingredientsNameSet;
    private Set<String> dishesNameSet;
    private List<ShortIngredient> ingredients;
    private List<ShortDish> dishes;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(Utils.ADD_INGREDIENT_TO_TABLE)) {
                ShortIngredient ingredient = (ShortIngredient) intent
                        .getSerializableExtra(Utils.INGREDIENT);
                assert ingredient != null;
                String ingredientName = ingredient.getIngredientName();
                if (ingredientsNameSet.contains(ingredientName)) {
                    sendItemCanBeAddedOnce();
                    return;
                }
                ingredientsNameSet.add(ingredientName);
                ingredients.add(new ShortIngredient(ingredientName, ingredient.getAmount()));
                addIngredientToTable(ingredient);
                sendItemAddedToDishContent();
            } else if (action.equals(Utils.ADD_DISH_TO_TABLE)) {
                ShortDish dish = (ShortDish) intent.getSerializableExtra(Utils.DISH);
                assert dish != null;
                String dishName = dish.getDishName();
                if (dishesNameSet.contains(dishName)) {
                    sendItemCanBeAddedOnce();
                    return;
                }
                dishesNameSet.add(dishName);
                dishes.add(dish);
                addDishToTable(dish);
                sendItemAddedToDishContent();
            }
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

    private View.OnClickListener itemsTableAddButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(AddDishActivity.this, SearchItemsActivity.class));
        }
    };

    private void displayAddFailed() {
        displayActionFailed(R.id.addDishButtonInfoText);
    }

    private void displayNameAlreadyUsed() {
        displayError(R.id.dishNameInformationText, nameAlreadyUsed);
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
        HttpManager.getRetrofitApi().insertNewDish(dishToInsert)
                .enqueue(new Callback<NameExistObject>() {
                    @Override
                    public void onResponse(@NonNull Call<NameExistObject> call,
                                           @NonNull Response<NameExistObject> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getNameExist()) {
                                displayNameAlreadyUsed();
                            } else {
                                Utils.displayToast(getApplicationContext(), dishAdded);
                                finish();
                            }
                        } else {
                            displayAddFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<NameExistObject> call,
                                          @NonNull Throwable t) {
                        displayAddFailed();
                    }
                });
    }

    private void sendItemCanBeAddedOnce() {
        sendBroadcast(new Intent(Utils.ITEM_CAN_BE_ADDED_ONCE));
    }

    private void sendItemAddedToDishContent() {
        sendBroadcast(new Intent(Utils.ITEM_ADDED_TO_DISH_CONTENT));
    }

    private void deleteIngredientFromList(String ingredientName) {
        Iterator<ShortIngredient> iterator = this.ingredients.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getIngredientName().equals(ingredientName)) {
                iterator.remove();
            }
        }
    }

    private void deleteDishFromList(String dishName) {
        Iterator<ShortDish> iterator = this.dishes.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getDishName().equals(dishName)) {
                iterator.remove();
            }
        }
    }

    private void deleteItemFromDataStructures(String itemName, boolean isIngredient) {
        if (isIngredient) {
            ingredientsNameSet.remove(itemName);
            deleteIngredientFromList(itemName);
        }
        else {
            dishesNameSet.remove(itemName);
            deleteDishFromList(itemName);
        }
    }

    private void addDeleteButtonToRow(TableRow row, final String itemName,
                                      final boolean isIngredient) {
        ImageView view = new ImageView(this);
        view.setImageResource(android.R.drawable.btn_dialog);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemFromDataStructures(itemName, isIngredient);
                View row = (View) v.getParent();
                ViewGroup container = ((ViewGroup)row.getParent());
                container.removeView(row);
                container.invalidate();
            }
        });
        view.setPadding(0, deletePadding, 0, 0);
        row.addView(view);
    }

    private void addTextViewToRow(TableRow row, String text, int textColorId,
                                  int viewWidth, int viewHeight, int viewWeight,
                                  int leftPadding, int backgroundColorId, boolean toCenter,
                                  int heightFactor) {
        TextView view = new TextView(this);
        viewHeight += (heightGaps * heightFactor);
        view.setLayoutParams(new TableRow.LayoutParams(viewWidth, viewHeight, viewWeight));
        if (toCenter) view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(leftPadding, 0, 0, 0);
        if (backgroundColorId != 0) view.setBackgroundColor(getColorById(backgroundColorId));
        if (!text.isEmpty()) {
            view.setTextColor(getColorById(textColorId));
            view.setText(text);
        }
        row.addView(view);
    }

    private void addItemToTable(String type, String itemName, String amount,
                                boolean toCenter, boolean isIngredient) {
        TableRow row = new TableRow(this);
        int heightFactor = (itemName.length() / maxNameSizeInRow);
        addTextViewToRow(row, type, R.color.lightBlue, typeWidth, itemRowHeight,
                smallWeight, typePadding, 0, toCenter, heightFactor);
        addTextViewToRow(row, itemName, R.color.black, nameWidth, itemRowHeight,
                bigWeight, namePadding, 0, toCenter, heightFactor);
        addTextViewToRow(row, amount, R.color.darkBlue, amountWidth, itemRowHeight,
                smallWeight, amountPadding, 0, toCenter, heightFactor);
        addDeleteButtonToRow(row, itemName, isIngredient);
        this.table.addView(row);
    }

    private void addDishToTable(ShortDish dish) {
        String name = dish.getDishName();
        String percent = Utils.doubleToPercent(dish.getPercent());
        boolean toCenter = (name.length() <= maxNameSizeInRow);
        addItemToTable(dishType, name, percent, toCenter, false);
    }

    private void addIngredientToTable(ShortIngredient ingredient) {
        String name = ingredient.getIngredientName();
        double amount = ingredient.getAmount();
        String amountStr = Utils.cleanDoubleToString(amount);
        boolean isAmountTextLong = (amountStr.length() > maxAmountSizeInRow)
                && ingredient.getIsLiquid();
        String gap = (isAmountTextLong) ? Utils.SPACE : Utils.EMPTY;
        amountStr += (gap + ingredient.getUnits());
        boolean toCenter = ((name.length() <= maxNameSizeInRow) && !isAmountTextLong);
        addItemToTable(ingredientType, name, amountStr, toCenter, true);
    }

    private void setTableTitles() {
        TableRow titlesRow = new TableRow(this);
        addTextViewToRow(titlesRow, typeTitle, R.color.black, typeWidth, titleRowHeight,
                smallWeight, typePadding, titleBackgroundColor, true, 0);
        addTextViewToRow(titlesRow, nameTitle, R.color.black, nameWidth, titleRowHeight,
                bigWeight, namePadding, titleBackgroundColor, true, 0);
        addTextViewToRow(titlesRow, amountTitle, R.color.black, amountWidth, titleRowHeight,
                smallWeight, amountPadding, titleBackgroundColor, true, 0);
        addTextViewToRow(titlesRow, Utils.EMPTY, R.color.black, deleteWidth, titleRowHeight,
                0, 0, titleBackgroundColor, true, 0);
        this.table.addView(titlesRow);
    }

    private void setComponents() {
        findViewById(R.id.addDishCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.addNewDishButton).setOnClickListener(this.addButtonClick);
        this.table = findViewById(R.id.itemsTable);
        setTableTitles();
        findViewById(R.id.itemsTableAddButton).setOnClickListener(
                this.itemsTableAddButtonClick);
        registerReceiver(broadcastReceiver, new IntentFilter(Utils.ADD_INGREDIENT_TO_TABLE));
        registerReceiver(broadcastReceiver, new IntentFilter(Utils.ADD_DISH_TO_TABLE));
        this.ingredientsNameSet = new HashSet<>();
        this.dishesNameSet = new HashSet<>();
        this.ingredients = new ArrayList<>();
        this.dishes = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dish_layout);
        setComponents();
    }
}
