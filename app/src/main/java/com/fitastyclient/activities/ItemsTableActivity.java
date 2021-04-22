package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.ShortDish;
import com.fitastyclient.data_holders.ShortIngredient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class ItemsTableActivity extends MyAppCompatActivity {

    static public int maxAmountSizeInRow = 4;
    static public int maxNameSizeInRow = 25;

    static public int itemRowHeight = 130;
    static public int typeWidth = 310;
    static public int typePadding = 20;
    static public int nameWidth = 800;
    static public int namePadding = 20;
    static public int amountWidth = 260;

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
                                          boolean disableDeleteButton, int infoTextId) {
        String ingredientName = ingredient.getIngredientName();
        this.ingredientsNameSet.add(ingredientName);
        if (keepIsLiquid) this.ingredients.add(ingredient);
        else this.ingredients.add(new ShortIngredient(ingredientName, ingredient.getAmount()));
        addIngredientToTable(ingredient, disableDeleteButton, infoTextId);
        if (sendItemAdded) sendItemAddedToDishContent();
    }

    protected void addDishToContent(ShortDish dish, boolean sendItemAdded,
                                    boolean disableDeleteButton, int infoTextId) {
        this.dishesNameSet.add(dish.getDishName());
        this.dishes.add(dish);
        addDishToTable(dish, disableDeleteButton, infoTextId);
        if (sendItemAdded) sendItemAddedToDishContent();
    }

    protected void sendItemCanBeAddedOnce() {
        sendBroadcast(new Intent(Utils.ITEM_CAN_BE_ADDED_ONCE));
    }

    protected void sendItemAddedToDishContent() {
        sendBroadcast(new Intent(Utils.ITEM_ADDED_TO_DISH_CONTENT));
    }

    protected void addDishToTable(ShortDish dish, boolean disableDeleteButton, int infoTextId) {
        String name = dish.getDishName();
        double percent = dish.getPercent();
        String percentStr = Utils.doubleToPercent(percent);
        boolean toCenter = (name.length() <= maxNameSizeInRow);
        addItemToTable(dishType, name, percentStr, toCenter, false,
                percent, disableDeleteButton, infoTextId);
    }

    protected void addIngredientToTable(ShortIngredient ingredient,
                                        boolean disableDeleteButton, int infoTextId) {
        String name = ingredient.getIngredientName();
        double amount = ingredient.getAmount();
        String amountStr = Utils.cleanDoubleToString(amount);
        boolean isAmountTextLong = (amountStr.length() > maxAmountSizeInRow)
                && ingredient.getIsLiquid();
        String gap = (isAmountTextLong) ? Utils.SPACE : Utils.EMPTY;
        amountStr += (gap + ingredient.getUnits());
        boolean toCenter = ((name.length() <= maxNameSizeInRow) && !isAmountTextLong);
        addItemToTable(ingredientType, name, amountStr, toCenter, true,
                amount, disableDeleteButton, infoTextId);
    }

    protected void addItemToTable(String type, String itemName, String amountStr,
                                  boolean toCenter, boolean isIngredient,
                                  double amount, boolean disableDeleteButton, int infoTextId) {
        TableRow row = new TableRow(this);
        int heightFactor = (itemName.length() / maxNameSizeInRow);
        addTextViewToRow(row, type, R.color.lightBlue, typeWidth, itemRowHeight,
                smallWeight, typePadding, toCenter, heightGaps, heightFactor, 0);
        addTextViewToRow(row, itemName, R.color.black, nameWidth, itemRowHeight,
                bigWeight, namePadding, toCenter, heightGaps, heightFactor, 0);
        addTextViewToRow(row, amountStr, R.color.darkBlue, amountWidth, itemRowHeight,
                smallWeight, 0, toCenter, heightGaps, heightFactor, 0);
        addInfoButtonToRow(row, itemName, isIngredient, infoTextId);
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

    protected void setItemsTableComponents(int tableId) {
        this.ingredientFlag = this.typeFlag + Utils.ADD_INGREDIENT_TO_TABLE;
        this.dishFlag = this.typeFlag + Utils.ADD_DISH_TO_TABLE;
        this.table = findViewById(tableId);
        this.ingredientsNameSet = new HashSet<>();
        this.dishesNameSet = new HashSet<>();
        this.ingredients = new ArrayList<>();
        this.dishes = new ArrayList<>();
        registerReceiver(getItemBroadcastReceiver(), new IntentFilter(this.ingredientFlag));
        registerReceiver(getItemBroadcastReceiver(), new IntentFilter(this.dishFlag));
    }

}
