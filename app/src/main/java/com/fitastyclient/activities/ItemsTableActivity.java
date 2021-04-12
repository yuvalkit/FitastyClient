package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;

import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.data_holders.ShortDish;
import com.fitastyclient.data_holders.ShortIngredient;
import com.fitastyclient.http.HttpManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ItemsTableActivity extends MyAppCompatActivity {

    static public String nameTitle = "Name";
    static public String amountTitle = "Amount";
    static public String typeTitle = "Type";

    static public int maxAmountSizeInRow = 4;
    static public int maxNameSizeInRow = 25;

    static public int itemRowHeight = 130;
    static public int typeWidth = 310;
    static public int typePadding = 20;
    static public int nameWidth = 820;
    static public int namePadding = 20;
    static public int amountWidth = 260;
    static public int amountPadding = 30;

    static public int titleRowHeight = 100;
    static public int titleBackgroundColor = R.color.veryLightBlue;
    static public int deleteWidth = 120;

    static public int bigWeight = 3;
    static public int smallWeight = 1;
    static public int heightGaps = 30;
    static public int deleteSize = 110;

    protected String typeFlag;
    protected String ingredientFlag;
    protected String dishFlag;
    protected TableLayout table;
    protected Set<String> ingredientsNameSet;
    protected Set<String> dishesNameSet;
    protected List<ShortIngredient> ingredients;
    protected List<ShortDish> dishes;

    abstract protected BroadcastReceiver getItemBroadcastReceiver();

    abstract protected void deleteItemFromContent(View view, String itemName,
                                                  boolean isIngredient, double amount);

    protected boolean checkIfItemCanBeAdded(Set<String> itemsNameSet, String ingredientName) {
        if (itemsNameSet.contains(ingredientName)) {
            sendItemCanBeAddedOnce();
            return false;
        }
        return true;
    }

    protected void addIngredientToContent(ShortIngredient ingredient,
                                          boolean keepIsLiquid, boolean sendItemAdded,
                                          boolean disableDeleteButton) {
        String ingredientName = ingredient.getIngredientName();
        this.ingredientsNameSet.add(ingredientName);
        if (keepIsLiquid) this.ingredients.add(ingredient);
        else this.ingredients.add(new ShortIngredient(ingredientName, ingredient.getAmount()));
        addIngredientToTable(ingredient, disableDeleteButton);
        if (sendItemAdded) sendItemAddedToDishContent();
    }

    protected void addDishToContent(ShortDish dish, boolean sendItemAdded,
                                    boolean disableDeleteButton) {
        this.dishesNameSet.add(dish.getDishName());
        this.dishes.add(dish);
        addDishToTable(dish, disableDeleteButton);
        if (sendItemAdded) sendItemAddedToDishContent();
    }

    protected void sendItemCanBeAddedOnce() {
        sendBroadcast(new Intent(Utils.ITEM_CAN_BE_ADDED_ONCE));
    }

    protected void sendItemAddedToDishContent() {
        sendBroadcast(new Intent(Utils.ITEM_ADDED_TO_DISH_CONTENT));
    }

    protected void addDishToTable(ShortDish dish, boolean disableDeleteButton) {
        String name = dish.getDishName();
        double percent = dish.getPercent();
        String percentStr = Utils.doubleToPercent(percent);
        boolean toCenter = (name.length() <= maxNameSizeInRow);
        addItemToTable(dishType, name, percentStr, toCenter, false,
                percent, disableDeleteButton);
    }

    protected void addIngredientToTable(ShortIngredient ingredient,
                                        boolean disableDeleteButton) {
        String name = ingredient.getIngredientName();
        double amount = ingredient.getAmount();
        String amountStr = Utils.cleanDoubleToString(amount);
        boolean isAmountTextLong = (amountStr.length() > maxAmountSizeInRow)
                && ingredient.getIsLiquid();
        String gap = (isAmountTextLong) ? Utils.SPACE : Utils.EMPTY;
        amountStr += (gap + ingredient.getUnits());
        boolean toCenter = ((name.length() <= maxNameSizeInRow) && !isAmountTextLong);
        addItemToTable(ingredientType, name, amountStr, toCenter, true,
                amount, disableDeleteButton);
    }

    protected void addItemToTable(String type, String itemName, String amountStr,
                                  boolean toCenter, boolean isIngredient,
                                  double amount, boolean disableDeleteButton) {
        TableRow row = new TableRow(this);
        int heightFactor = (itemName.length() / maxNameSizeInRow);
        addTextViewToRow(row, type, R.color.lightBlue, typeWidth, itemRowHeight,
                smallWeight, typePadding, 0, toCenter, heightGaps, heightFactor, 0);
        addTextViewToRow(row, itemName, R.color.black, nameWidth, itemRowHeight,
                bigWeight, namePadding, 0, toCenter, heightGaps, heightFactor, 0);
        addTextViewToRow(row, amountStr, R.color.darkBlue, amountWidth, itemRowHeight,
                smallWeight, amountPadding, 0, toCenter, heightGaps, heightFactor, 0);
        addDeleteButtonToRow(row, itemName, isIngredient, amount, disableDeleteButton);
        this.table.addView(row);
    }

    protected void addDeleteButtonToRow(TableRow row, final String itemName,
                                        final boolean isIngredient, final double amount,
                                        boolean disableDeleteButton) {
        ImageView view = getImageView(android.R.drawable.btn_dialog, deleteSize, 0, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemFromContent(view, itemName, isIngredient, amount);
            }
        });
        if (disableDeleteButton) view.setVisibility(View.INVISIBLE);
        row.addView(view);
    }

    protected void deleteItemFromDataStructures(String itemName, boolean isIngredient) {
        if (isIngredient) {
            ingredientsNameSet.remove(itemName);
            deleteIngredientFromList(itemName);
        }
        else {
            dishesNameSet.remove(itemName);
            deleteDishFromList(itemName);
        }
    }

    protected void deleteIngredientFromList(String ingredientName) {
        Iterator<ShortIngredient> iterator = this.ingredients.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getIngredientName().equals(ingredientName)) {
                iterator.remove();
            }
        }
    }

    protected void deleteDishFromList(String dishName) {
        Iterator<ShortDish> iterator = this.dishes.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getDishName().equals(dishName)) {
                iterator.remove();
            }
        }
    }

    protected void setTableTitles() {
        TableRow titlesRow = new TableRow(this);
        addTextViewToRow(titlesRow, typeTitle, R.color.black, typeWidth, titleRowHeight,
                smallWeight, typePadding, titleBackgroundColor, true, 0, 0, 0);
        addTextViewToRow(titlesRow, nameTitle, R.color.black, nameWidth, titleRowHeight,
                bigWeight, namePadding, titleBackgroundColor, true, 0, 0, 0);
        addTextViewToRow(titlesRow, amountTitle, R.color.black, amountWidth, titleRowHeight,
                smallWeight, amountPadding, titleBackgroundColor, true, 0, 0, 0);
        addEmptyTextViewToRow(titlesRow, deleteWidth, titleRowHeight, titleBackgroundColor);
        this.table.addView(titlesRow);
    }

    protected void setItemsTableComponents(int tableId) {
        this.ingredientFlag = this.typeFlag + Utils.ADD_INGREDIENT_TO_TABLE;
        this.dishFlag = this.typeFlag + Utils.ADD_DISH_TO_TABLE;
        this.table = findViewById(tableId);
        this.ingredientsNameSet = new HashSet<>();
        this.dishesNameSet = new HashSet<>();
        this.ingredients = new ArrayList<>();
        this.dishes = new ArrayList<>();
        setTableTitles();
        registerReceiver(getItemBroadcastReceiver(), new IntentFilter(this.ingredientFlag));
        registerReceiver(getItemBroadcastReceiver(), new IntentFilter(this.dishFlag));
    }

}
