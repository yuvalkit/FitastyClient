package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Account;
import com.fitastyclient.data_holders.CalorieInfo;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.data_holders.NutritionFactsFilter;
import com.fitastyclient.data_holders.ShortDish;
import com.fitastyclient.data_holders.ShortIngredient;
import com.fitastyclient.data_holders.SearchBody;
import com.fitastyclient.data_holders.SearchResult;
import com.fitastyclient.dialogs.DishInfoDialog;
import com.fitastyclient.dialogs.NutritionFactsFilterDialog;
import com.fitastyclient.http.HttpManager;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchItemsActivity extends MyAppCompatActivity {

    static public int typeWidth = 500;
    static public int nameWidth = 800;
    static public int itemRowHeight = 130;

    static public int editTextTextSize = 15;
    static public int viewWeight = 1;
    static public int viewLeftPadding = 20;

    static public int maxNameSizeInRow = 22;
    static public int heightGaps = 60;

    static public int infoIconSize = 90;
    static public int infoIconLeftPadding = 10;
    static public int addIconSize = 120;

    static public String mustChooseWhatToSearch = "You must choose what to search for.";
    static public String searchFailed = "Search failed, please try again.";
    static public String mustEnterAmount = "You must enter an amount.";
    static public String itemCanBeAddedOnce = "Each item can be added only once.";
    static public String itemAddedToDishContent = "Item added to dish content.";
    static public String noItemsFound = "No items found.";
    static public String amountStringFormat = "Amount (%s)";

    private TableLayout table;
    private CalorieInfo recommendedFacts;
    private NutritionFactsFilter factsFilter;
    private boolean isAddMeal;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(Utils.ITEM_CAN_BE_ADDED_ONCE)) {
                displaySearchInfoError(itemCanBeAddedOnce);
            } else if (action.equals(Utils.ITEM_ADDED_TO_DISH_CONTENT)) {
                setViewTextAndColor(R.id.searchItemsInfoText,
                        itemAddedToDishContent, R.color.green);
            } else if (action.equals(Utils.UPDATE_FACTS_FILTER)) {
                factsFilter = (NutritionFactsFilter)
                        intent.getSerializableExtra(Utils.FACTS_FILTER);
            } else if (action.equals(Utils.ITEM_ADD_FAILED)) {
                displayItemInfoFailed();
            }
        }
    };

    private View.OnClickListener searchButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearSearchInfoText();
            search();
        }
    };

    private View.OnClickListener nutritionFactsFilterButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            showDialogFragment(new NutritionFactsFilterDialog(
                    factsFilter, recommendedFacts, isAddMeal));
        }
    };


    private SearchBody getSearchBodyFromFields() {
        String nameBegin = getTextFromView(R.id.searchItemInput);
        boolean isVegan = isCheckBoxChecked(R.id.veganCheckBox);
        boolean isVegetarian = isCheckBoxChecked(R.id.vegetarianCheckBox);
        boolean isGlutenFree = isCheckBoxChecked(R.id.glutenFreeCheckBox);
        boolean isLactoseFree = isCheckBoxChecked(R.id.lactoseFreeCheckBox);
        return new SearchBody(nameBegin, isVegan, isVegetarian, isGlutenFree,
                isLactoseFree, this.factsFilter);
    }

    private void clearSearchInfoText() {
        clearInformationText(R.id.searchItemsInfoText);
    }

    private void displaySearchInfoError(String text) {
        displayError(R.id.searchItemsInfoText, text);
    }

    private void displaySearchFailed() {
        displaySearchInfoError(searchFailed);
    }

    private void displayItemInfoFailed() {
        displayActionFailed(R.id.searchItemsInfoText);
    }

    private void displayNoItemsFound() {
        setViewTextAndColor(R.id.searchItemsInfoText, noItemsFound, R.color.gray);
    }

    private void addSearchResultToTable(SearchResult searchResult) {
        this.table.removeAllViews();
        List<ShortIngredient> ingredients = searchResult.getIngredients();
        List<ShortDish> dishes = searchResult.getDishes();
        if (ingredients.isEmpty() && dishes.isEmpty()) {
            displayNoItemsFound();
            return;
        }
        for (ShortIngredient ingredient : ingredients) {
            addItemToTable(ingredient.getIngredientName(), true, ingredient.getIsLiquid());
        }
        for (ShortDish dish : dishes) {
            addItemToTable(dish.getDishName(), false, false);
        }
    }

    private void search() {
        boolean includeDishes = isCheckBoxChecked(R.id.dishesCheckBox);
        boolean includeIngredients = isCheckBoxChecked(R.id.ingredientsCheckBox);
        if (!includeDishes && !includeIngredients) {
            displaySearchInfoError(mustChooseWhatToSearch);
            return;
        }
        final SearchBody searchBody = getSearchBodyFromFields();
        HttpManager.getRetrofitApiWithNulls().getFoods(
                includeDishes, includeIngredients, searchBody)
                .enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(@NonNull Call<SearchResult> call,
                                           @NonNull Response<SearchResult> response) {
                        if (response.isSuccessful()) {
                            SearchResult searchResult = response.body();
                            assert searchResult != null;
                            addSearchResultToTable(searchResult);
                        } else {
                            displaySearchFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<SearchResult> call,
                                          @NonNull Throwable t) {
                        displaySearchFailed();
                    }
                });
    }

    private <T> void sendGetItemInfoRequest(Call<T> call, final boolean isIngredient) {
        call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(@NonNull Call<T> call,
                                           @NonNull Response<T> response) {
                        if (response.isSuccessful()) {
                            clearSearchInfoText();
                            T item = response.body();
                            assert item != null;
                            if (isIngredient) displayIngredientInfoDialog((Ingredient) item);
                            else displayDishInfoDialog((Dish) item);
                        } else {
                            displayItemInfoFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<T> call,
                                          @NonNull Throwable t) {
                        displayItemInfoFailed();
                    }
                });
    }

    private void getIngredientInfo(String ingredientName) {
        sendGetItemInfoRequest(HttpManager.getRetrofitApi()
                .getIngredientInfo(ingredientName), true);
    }

    private void getDishInfo(String dishName) {
        sendGetItemInfoRequest(HttpManager.getRetrofitApi()
                .getDishInfo(dishName), false);
    }

    private void getItemInfo(String itemName, boolean isIngredient) {
        if (isIngredient) getIngredientInfo(itemName);
        else getDishInfo(itemName);
    }

    private void sendItemToParentActivity(String itemName, final boolean isIngredient,
                                           boolean isLiquid, double amount) {
        String flag = (this.isAddMeal) ? Utils.MEAL_FLAG : Utils.DISH_FLAG;
        flag += (isIngredient) ? Utils.ADD_INGREDIENT_TO_TABLE : Utils.ADD_DISH_TO_TABLE;
        Intent intent = new Intent(flag);
        if (isIngredient) {
            intent.putExtra(Utils.INGREDIENT, new ShortIngredient(itemName, isLiquid, amount));
        } else {
            intent.putExtra(Utils.DISH, new ShortDish(itemName, amount));
        }
        sendBroadcast(intent);
    }

    private void addAddButtonToRow(TableRow row, final String itemName, final boolean isIngredient,
                                   final boolean isLiquid, final EditText editText) {
        ImageView view = getImageView(android.R.drawable.ic_input_add, addIconSize, 0, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountText = editText.getText().toString();
                if (amountText.isEmpty()) {
                    displaySearchInfoError(mustEnterAmount);
                    return;
                }
                clearSearchInfoText();
                double amount = Double.parseDouble(amountText);
                if (!isIngredient) amount /= 100;
                sendItemToParentActivity(itemName, isIngredient, isLiquid, amount);
            }
        });
        row.addView(view);
    }

    private void addInfoButtonToRow(TableRow row, final String itemName,
                                    final boolean isIngredient) {
        ImageView view = getImageView(android.R.drawable.ic_dialog_info,
                infoIconSize, R.color.blue, infoIconLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemInfo(itemName, isIngredient);
            }
        });
        row.addView(view);
    }

    private EditText getAmountInputBar(boolean isIngredient, boolean isLiquid) {
        EditText editText = new EditText(this);
        String units = (isIngredient) ? ((isLiquid) ? Utils.ML : Utils.GRAM) : Utils.PERCENT;
        String amountText = String.format(amountStringFormat, units);
        editText.setHint(amountText);
        editText.setTextSize(editTextTextSize);
        int inputType = InputType.TYPE_CLASS_NUMBER;
        if (isIngredient) inputType |= InputType.TYPE_NUMBER_FLAG_DECIMAL;
        editText.setInputType(inputType);
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(6)});
        return editText;
    }

    private void addItemToTable(String itemName, boolean isIngredient, boolean isLiquid) {
        TableRow row = new TableRow(this);
        String type = (isIngredient) ? ingredientType : dishType;
        int heightFactor = (itemName.length() / maxNameSizeInRow);
        addInfoButtonToRow(row, itemName, isIngredient);
        addTextViewToRow(row, type, R.color.lightBlue, typeWidth, itemRowHeight, viewWeight,
                viewLeftPadding, 0, false, heightGaps, heightFactor, 0);
        addTextViewToRow(row, itemName, R.color.black, nameWidth, itemRowHeight, viewWeight,
                viewLeftPadding, 0, false, heightGaps, heightFactor, 0);
        EditText editText = getAmountInputBar(isIngredient, isLiquid);
        row.addView(editText);
        addAddButtonToRow(row, itemName, isIngredient, isLiquid, editText);
        this.table.addView(row);
    }

    private void setComponents() {
        findViewById(R.id.searchItemsCancelIcon).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.searchButton).setOnClickListener(this.searchButtonClick);
        findViewById(R.id.nutritionFactsFilterButton)
                .setOnClickListener(this.nutritionFactsFilterButtonClick);
        this.table = findViewById(R.id.searchedItemsTable);
        this.isAddMeal = Objects.requireNonNull(getIntent().getExtras())
                .getBoolean(Utils.IS_ADD_MEAL);
        if (this.isAddMeal) {
            this.recommendedFacts = (CalorieInfo) getIntent()
                    .getSerializableExtra(Utils.CALORIE_INFO);
            assert recommendedFacts != null;
            this.factsFilter = new NutritionFactsFilter(this.recommendedFacts);
        } else {
            this.factsFilter = new NutritionFactsFilter();
        }
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.ITEM_CAN_BE_ADDED_ONCE));
        registerReceiver(this.broadcastReceiver,
                new IntentFilter(Utils.ITEM_ADDED_TO_DISH_CONTENT));
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.UPDATE_FACTS_FILTER));
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.ITEM_ADD_FAILED));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_items_layout);
        setComponents();
    }

}
