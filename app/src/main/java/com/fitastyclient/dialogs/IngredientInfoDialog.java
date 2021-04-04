package com.fitastyclient.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Ingredient;

public class IngredientInfoDialog extends MyDialogFragment {

    private Ingredient ingredient;

    protected void populateFieldsFromView(View view) {
        String name = this.ingredient.getIngredientName();
        String fatStr = Utils.cleanDoubleToString(this.ingredient.getFat());
        String carbStr = Utils.cleanDoubleToString(this.ingredient.getCarb());
        String fiberStr = Utils.cleanDoubleToString(this.ingredient.getFiber());
        String proteinStr = Utils.cleanDoubleToString(this.ingredient.getProtein());
        String servingStr = Utils.cleanDoubleToString(this.ingredient.getServing());
        boolean isVegan = this.ingredient.getIsVegan();
        boolean isVegetarian = this.ingredient.getIsVegetarian();
        boolean isGlutenFree = this.ingredient.getIsGlutenFree();
        boolean isLactoseFree = this.ingredient.getIsLactoseFree();
        String units = this.ingredient.getUnits();
        String nutritionFactsTitle = Utils.factsPer100prefix + units + Utils.COLON;
        setViewText(view, R.id.ingredientInfoNameText, name);
        setViewText(view, R.id.ingredientInfoServingText, servingStr + units);
        setViewText(view, R.id.ingredientInfoNutritionFactsTitle, nutritionFactsTitle);
        setViewText(view, R.id.ingredientInfoFatText, fatStr + Utils.GRAM);
        setViewText(view, R.id.ingredientInfoCarbText, carbStr + Utils.GRAM);
        setViewText(view, R.id.ingredientInfoFiberText, fiberStr + Utils.GRAM);
        setViewText(view, R.id.ingredientInfoProteinText, proteinStr + Utils.GRAM);
        if (isVegan) enableCheckBox(view, R.id.ingredientInfoVeganCheckBox);
        if (isVegetarian) enableCheckBox(view, R.id.ingredientInfoVegetarianCheckBox);
        if (isGlutenFree) enableCheckBox(view, R.id.ingredientInfoGlutenFreeCheckBox);
        if (isLactoseFree) enableCheckBox(view, R.id.ingredientInfoLactoseFreeCheckBox);
    }

    public IngredientInfoDialog(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return getDialog(R.layout.ingredient_info_dialog);
    }
}
