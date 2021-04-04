package com.fitastyclient.activities;

import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.dialogs.DishInfoDialog;
import com.fitastyclient.dialogs.IngredientInfoDialog;

import org.w3c.dom.Text;

import java.util.Locale;

public abstract class MyAppCompatActivity extends AppCompatActivity {

    static public String requiredField = "This field is required.";
    static public String errorOccurred = "An error occurred.";
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

    private void showDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    protected void displayIngredientInfoDialog(Ingredient ingredient) {
        showDialogFragment(new IngredientInfoDialog(ingredient));
    }

    protected void displayDishInfoDialog(Dish dish) {
        showDialogFragment(new DishInfoDialog(dish));
    }
}
