package com.fitastyclient.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.data_holders.ShortIngredient;
import com.fitastyclient.http.HttpManager;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishInfoDialog extends MyDialogFragment {

    public static int iconSize = 70;
    public static int leftPadding = 10;
    public static int textHeight = 120;
    static public int heightGaps = 40;
    static public int maxNameSizeInRow = 22;

    public static int nameWidth = 150;
    public static int nameWeight = 3;

    public static int amountWidth = 80;
    public static int amountWeight = 1;

    private Dish dish;
    private TableLayout table;

    private void clearIngredientInfo(View view) {
        setViewText(view, R.id.dishInfoIngredientInfoText, Utils.EMPTY);
    }

    private void displayIngredientInfoFailed(View view) {
        setViewText(view, R.id.dishInfoIngredientInfoText, Utils.actionFailed);
    }

    private void getIngredientInfo(String ingredientName, final View view) {
        HttpManager.getRetrofitApi().getIngredientInfo(ingredientName)
                .enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(@NonNull Call<Ingredient> call,
                                   @NonNull Response<Ingredient> response) {
                if (response.isSuccessful()) {
                    Ingredient ingredient = response.body();
                    assert ingredient != null;
                    DialogFragment dialogFragment = new IngredientInfoDialog(ingredient);
                    dialogFragment.show((Objects.requireNonNull(getActivity()))
                            .getSupportFragmentManager().beginTransaction(), null);
                } else {
                    displayIngredientInfoFailed(view);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Ingredient> call,
                                  @NonNull Throwable t) {
                displayIngredientInfoFailed(view);
            }
        });
    }

    private void addInfoButtonToRow(TableRow row, final String ingredientName, final View view) {
        final ImageView imageView = new ImageView(getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(iconSize, iconSize);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(android.R.drawable.ic_dialog_info);
        imageView.setColorFilter(getColorById(R.color.blue),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearIngredientInfo(view);
                getIngredientInfo(ingredientName, view);
            }
        });
        row.addView(imageView);
    }

    private void addTextViewToRow(TableRow row, String text, int width, int weight,
                                  int heightFactor) {
        TextView view = new TextView(getContext());
        int height = textHeight + (heightGaps * heightFactor);
        view.setLayoutParams(new TableRow.LayoutParams(width, height, weight));
        if (heightFactor == 0) view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(leftPadding, 0, 0, 0);
        view.setTextColor(getColorById(R.color.black));
        view.setText(text);
        row.addView(view);
    }

    private void addIngredientToTable(ShortIngredient ingredient, View view) {
        String ingredientName = ingredient.getIngredientName();
        String amountStr = Utils.cleanDoubleToString(ingredient.getAmount());
        String units = ingredient.getUnits();
        TableRow row = new TableRow(getContext());
        int heightFactor = (ingredientName.length() / maxNameSizeInRow);
        addTextViewToRow(row, ingredientName, nameWidth, nameWeight, heightFactor);
        addTextViewToRow(row, amountStr + units, amountWidth, amountWeight, heightFactor);
        addInfoButtonToRow(row, ingredientName, view);
        this.table.addView(row);
    }

    protected void populateFieldsFromView(View view) {
        String dishName = this.dish.getDishName();
        String fatStr = Utils.cleanDoubleToString(this.dish.getFat());
        String carbStr = Utils.cleanDoubleToString(this.dish.getCarb());
        String fiberStr = Utils.cleanDoubleToString(this.dish.getFiber());
        String proteinStr = Utils.cleanDoubleToString(this.dish.getProtein());
        setViewText(view, R.id.dishInfoNameText, dishName);
        setViewText(view, R.id.dishInfoFatText, fatStr + Utils.GRAM);
        setViewText(view, R.id.dishInfoCarbText, carbStr + Utils.GRAM);
        setViewText(view, R.id.dishInfoFiberText, fiberStr + Utils.GRAM);
        setViewText(view, R.id.dishInfoProteinText, proteinStr + Utils.GRAM);
        List<ShortIngredient> ingredients = this.dish.getIngredients();
        this.table = view.findViewById(R.id.dishInfoIngredientsTable);
        for (ShortIngredient ingredient : ingredients) {
            addIngredientToTable(ingredient, view);
        }
    }

    public DishInfoDialog(Dish dish) {
        this.dish = dish;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return getDialog(R.layout.dish_info_dialog);
    }

}
