package com.fitastyclient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.dialogs.DishInfoDialog;
import com.fitastyclient.dialogs.IngredientInfoDialog;

public abstract class MyAppCompatActivity extends AppCompatActivity {

    static public String requiredField = "This field is required.";
    static public String ingredientType = "Ingredient";
    static public String dishType = "Dish";

    protected View.OnClickListener cancelButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    protected void setViewText(int viewId, String text) {
        ((TextView) findViewById(viewId)).setText(text);
    }

    protected void clearInformationText(int textViewId) {
        setViewText(textViewId, Utils.EMPTY);
    }

    protected void setViewTextAndColor(int viewId, String text, int colorName) {
        TextView view = findViewById(viewId);
        view.setTextColor(getResources().getColor(colorName));
        view.setText(text);
    }

    protected void displayError(int viewId, String text) {
        setViewTextAndColor(viewId, text, R.color.red);
    }

    protected void displayFieldIsRequired(int viewId) {
        displayError(viewId, requiredField);
    }

    protected void displayActionFailed(int viewId) {
        displayError(viewId, Utils.actionFailed);
    }

    protected String getTextFromView(int viewId) {
        EditText editText = findViewById(viewId);
        return editText.getText().toString();
    }

    protected void toggleRadioButton(int radioButtonId) {
        ((RadioButton) findViewById(radioButtonId)).toggle();
    }

    protected boolean isRadioButtonChecked(int radioButtonId) {
        return ((RadioButton) findViewById(radioButtonId)).isChecked();
    }

    protected boolean isCheckBoxChecked(int checkBoxId) {
        return ((CheckBox) findViewById(checkBoxId)).isChecked();
    }

    protected boolean isFieldEmptyQuery(int viewId, int InfoId) {
        String text = getTextFromView(viewId);
        if (text.isEmpty()) {
            displayFieldIsRequired(InfoId);
            return true;
        } else {
            clearInformationText(InfoId);
            return false;
        }
    }

    protected void setSpinner(Spinner spinner, int stringsArrayId, int selection_index) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                stringsArrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selection_index);
    }

    protected int getColorById(int colorId) {
        return getResources().getColor(colorId);
    }

    protected void showDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    protected void displayIngredientInfoDialog(Ingredient ingredient) {
        showDialogFragment(new IngredientInfoDialog(ingredient));
    }

    protected void displayDishInfoDialog(Dish dish) {
        showDialogFragment(new DishInfoDialog(dish));
    }

    protected void addTextViewToRow(TableRow row, String text, int textColorId,
                                  int viewWidth, int viewHeight, int viewWeight,
                                  int leftPadding, int backgroundColorId, boolean toCenter,
                                  int heightGaps, int heightFactor) {
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

    protected void setViewWithValue(int viewId, double value) {
        setViewText(viewId, Utils.cleanDoubleToString(value));
    }

    protected void setViewWithFact(int viewId, double value) {
        setViewText(viewId, Utils.cleanDoubleToString(value) + Utils.GRAM);
    }

    protected Intent getIntentWithBooleanFlag(Context fromContext, Class<?> toClass,
                                              String flagText, boolean flagValue) {
        Intent intent = new Intent(fromContext, toClass);
        Bundle bundle = new Bundle();
        bundle.putBoolean(flagText, flagValue);
        intent.putExtras(bundle);
        return intent;
    }

}
