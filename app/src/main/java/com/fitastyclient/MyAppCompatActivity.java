package com.fitastyclient;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public abstract class MyAppCompatActivity extends AppCompatActivity {

    static public String requiredField = "This field is required.";
    static public String factsPer100prefix = "Nutrition facts per 100";
    static public String errorOccurred = "An error occurred.";

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

    protected void displayFieldIsRequired(int textViewId) {
        setViewTextAndColor(textViewId, requiredField, R.color.red);
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
}
