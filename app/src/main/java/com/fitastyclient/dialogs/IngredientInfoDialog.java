package com.fitastyclient.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Ingredient;


public class IngredientInfoDialog extends MyDialogFragment {

    private Ingredient ingredient;

    private void populateFields() {
        String name = this.ingredient.getIngredientName();
        String servingStr = Utils.cleanDoubleToString(this.ingredient.getServing());
        boolean isVegan = this.ingredient.getIsVegan();
        boolean isVegetarian = this.ingredient.getIsVegetarian();
        boolean isGlutenFree = this.ingredient.getIsGlutenFree();
        boolean isLactoseFree = this.ingredient.getIsLactoseFree();
        String units = this.ingredient.getUnits();
        String nutritionFactsTitle = Utils.FACTS_PER_100_PREFIX + units + Utils.COLON;
        setViewText(R.id.ingredientInfoNameText, name);
        setViewText(R.id.ingredientInfoServingText, servingStr + units);
        setViewText(R.id.ingredientInfoNutritionFactsTitle, nutritionFactsTitle);
        setViewToGramsValue(R.id.ingredientInfoFatText, this.ingredient.getFat());
        setViewToGramsValue(R.id.ingredientInfoCarbText, this.ingredient.getCarb());
        setViewToGramsValue(R.id.ingredientInfoFiberText, this.ingredient.getFiber());
        setViewToGramsValue(R.id.ingredientInfoProteinText, this.ingredient.getProtein());
        if (isVegan) enableCheckBox(R.id.ingredientInfoVeganCheckBox);
        if (isVegetarian) enableCheckBox(R.id.ingredientInfoVegetarianCheckBox);
        if (isGlutenFree) enableCheckBox(R.id.ingredientInfoGlutenFreeCheckBox);
        if (isLactoseFree) enableCheckBox(R.id.ingredientInfoLactoseFreeCheckBox);
    }

    public IngredientInfoDialog(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = getBuilder();
        setInflaterView(R.layout.ingredient_info_dialog);
        setCloseButton(builder);
        populateFields();
        return builder.create();
    }
}
