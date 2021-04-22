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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.data_holders.ShortIngredient;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishInfoDialog extends MyDialogFragment {

    public static int iconSize = 80;
    public static int padding = 10;
    public static int textHeight = 120;
    public static int heightGaps = 40;
    public static int maxNameSizeInRow = 22;

    public static int nameWidth = 150;
    public static int nameWeight = 3;

    public static int amountWidth = 80;
    public static int amountWeight = 1;

    private Dish dish;
    private TableLayout table;

    private void clearIngredientInfo() {
        setViewText(R.id.dishInfoIngredientInfoText, Utils.EMPTY);
    }

    private void displayIngredientInfoFailed() {
        setViewText(R.id.dishInfoIngredientInfoText, Utils.ACTION_FAILED);
    }

    private void getIngredientInfo(String ingredientName) {
        Utils.getRetrofitApi().getIngredientInfo(ingredientName)
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Ingredient ingredient = Utils.getResponseObject(response, Ingredient.class);
                    if (ingredient != null) {
                        DialogFragment dialogFragment = new IngredientInfoDialog(ingredient);
                        dialogFragment.show((Objects.requireNonNull(getActivity()))
                                .getSupportFragmentManager().beginTransaction(), null);
                    } else {
                        displayIngredientInfoFailed();
                    }
                } else {
                    displayIngredientInfoFailed();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                displayIngredientInfoFailed();
            }
        });
    }

    private void addInfoButtonToRow(TableRow row, final String ingredientName) {
        final ImageView imageView = new ImageView(getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(iconSize, iconSize);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(android.R.drawable.ic_dialog_info);
        imageView.setColorFilter(getColorById(R.color.blue),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        imageView.setPadding(0, 0, padding, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearIngredientInfo();
                getIngredientInfo(ingredientName);
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
        view.setPadding(padding, 0, 0, 0);
        view.setTextColor(getColorById(R.color.black));
        view.setText(text);
        row.addView(view);
    }

    private void addIngredientToTable(ShortIngredient ingredient) {
        String ingredientName = ingredient.getIngredientName();
        String amountStr = Utils.cleanDoubleToString(ingredient.getAmount());
        String units = ingredient.getUnits();
        TableRow row = new TableRow(getContext());
        int heightFactor = (ingredientName.length() / maxNameSizeInRow);
        addTextViewToRow(row, ingredientName, nameWidth, nameWeight, heightFactor);
        addTextViewToRow(row, amountStr + units, amountWidth, amountWeight, heightFactor);
        addInfoButtonToRow(row, ingredientName);
        this.table.addView(row);
    }

    private void populateFields() {
        String dishName = this.dish.getDishName();
        setViewText(R.id.dishInfoNameText, dishName);
        setViewToGramsValue(R.id.dishInfoFatText, this.dish.getFat());
        setViewToGramsValue(R.id.dishInfoCarbText, this.dish.getCarb());
        setViewToGramsValue(R.id.dishInfoFiberText, this.dish.getFiber());
        setViewToGramsValue(R.id.dishInfoProteinText, this.dish.getProtein());
        List<ShortIngredient> ingredients = this.dish.getIngredients();
        this.table = this.view.findViewById(R.id.dishInfoIngredientsTable);
        for (ShortIngredient ingredient : ingredients) {
            addIngredientToTable(ingredient);
        }
    }

    public DishInfoDialog(Dish dish) {
        this.dish = dish;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = getBuilder();
        setInflaterView(R.layout.dish_info_dialog);
        setCloseButton(builder);
        populateFields();
        return builder.create();
    }

}
