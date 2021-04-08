package com.fitastyclient.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.activities.LoginActivity;
import com.fitastyclient.activities.NewEditAccountActivity;
import com.fitastyclient.data_holders.CalorieInfo;
import com.fitastyclient.data_holders.NutritionFactsFilter;
import java.util.Objects;

public class NutritionFactsFilterDialog extends MyDialogFragment {

    public static String recommendedMaxValues = "These max values are recommended for you.";

    private NutritionFactsFilter factsFilter;
    private CalorieInfo recommendedFacts;
    private boolean isAddMeal;
    private EditText maxFatEditText;
    private EditText maxCarbEditText;
    private EditText maxFiberEditText;
    private EditText maxProteinEditText;
    private TextWatcher textWatcher;

    private View.OnClickListener resetButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            setFactsFields(new NutritionFactsFilter(recommendedFacts), false);
            setRecommendedMaxValuesText(recommendedMaxValues);
            setTextChangedListeners();
        }
    };

    private void setViewDoubleWithNulls(int viewId, Double value) {
        if (value != null) {
            setViewText(viewId, Utils.cleanDoubleToString(value));
        } else {
            setViewText(viewId, Utils.EMPTY);
        }
    }

    private void setFactsFields(NutritionFactsFilter filter, boolean toSetMinValues) {
        setViewDoubleWithNulls(R.id.fatMaxValue, filter.getMaxFat());
        setViewDoubleWithNulls(R.id.carbMaxValue, filter.getMaxCarb());
        setViewDoubleWithNulls(R.id.fiberMaxValue, filter.getMaxFiber());
        setViewDoubleWithNulls(R.id.proteinMaxValue, filter.getMaxProtein());
        if (toSetMinValues) {
            setViewDoubleWithNulls(R.id.fatMinValue, filter.getMinFat());
            setViewDoubleWithNulls(R.id.carbMinValue, filter.getMinCarb());
            setViewDoubleWithNulls(R.id.fiberMinValue, filter.getMinFiber());
            setViewDoubleWithNulls(R.id.proteinMinValue, filter.getMinProtein());
        }
    }

    private void populateFields() {
        setFactsFields(this.factsFilter, true);
        this.maxFatEditText = this.view.findViewById(R.id.fatMaxValue);
        this.maxCarbEditText = this.view.findViewById(R.id.carbMaxValue);
        this.maxFiberEditText = this.view.findViewById(R.id.fiberMaxValue);
        this.maxProteinEditText = this.view.findViewById(R.id.proteinMaxValue);
        this.textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setRecommendedMaxValuesText(Utils.EMPTY);
                removeTextChangedListeners();
            }};
        if (this.isAddMeal) {
            setResetButton();
            if (isFactsFilerHasRecommendedFacts()) {
                setRecommendedMaxValuesText(recommendedMaxValues);
            }
            setTextChangedListeners();
        }
    }

    private boolean isFactsFilerHasRecommendedFacts() {
        Double maxFat = this.factsFilter.getMaxFat();
        Double maxCarb = this.factsFilter.getMaxCarb();
        Double maxFiber = this.factsFilter.getMaxFiber();
        Double maxProtein = this.factsFilter.getMaxProtein();
        if ((maxFat == null) || (maxCarb == null) || (maxFiber == null) || (maxProtein == null)) {
            return false;
        }
        return (maxFat == this.recommendedFacts.getFat())
                && (maxCarb == this.recommendedFacts.getCarb())
                && (maxFiber == this.recommendedFacts.getFiber())
                && (maxProtein == this.recommendedFacts.getProtein());
    }

    private void setResetButton() {
        Button resetButton = this.view.findViewById(R.id.resetToRecommendedButton);
        resetButton.setVisibility(View.VISIBLE);
        resetButton.setOnClickListener(this.resetButtonClick);
    }

    private void setTextChangedListeners() {
        setMaxValueTextChanged(this.maxFatEditText);
        setMaxValueTextChanged(this.maxCarbEditText);
        setMaxValueTextChanged(this.maxFiberEditText);
        setMaxValueTextChanged(this.maxProteinEditText);
    }

    private void removeTextChangedListeners() {
        removeMaxValueTextChanged(this.maxFatEditText);
        removeMaxValueTextChanged(this.maxCarbEditText);
        removeMaxValueTextChanged(this.maxFiberEditText);
        removeMaxValueTextChanged(this.maxProteinEditText);
    }

    private void setMaxValueTextChanged(EditText valueEditText) {
        valueEditText.addTextChangedListener(this.textWatcher);
    }

    private void removeMaxValueTextChanged(EditText valueEditText) {
        valueEditText.removeTextChangedListener(this.textWatcher);
    }

    private String getTextFromView(int viewId) {
        EditText editText = this.view.findViewById(viewId);
        return editText.getText().toString();
    }

    private Double getDoubleFromTextWithNulls(String text) {
        if (!text.isEmpty()) {
            return Double.parseDouble(text);
        } else {
            return null;
        }
    }

    private Double getDoubleFromViewWithNulls(int viewId) {
        return getDoubleFromTextWithNulls(getTextFromView(viewId));
    }

    private NutritionFactsFilter getFactsFilterFromFields() {
        Double maxFat = getDoubleFromViewWithNulls(R.id.fatMaxValue);
        Double maxCarb = getDoubleFromViewWithNulls(R.id.carbMaxValue);
        Double maxFiber = getDoubleFromViewWithNulls(R.id.fiberMaxValue);
        Double maxProtein = getDoubleFromViewWithNulls(R.id.proteinMaxValue);
        Double minFat = getDoubleFromViewWithNulls(R.id.fatMinValue);
        Double minCarb = getDoubleFromViewWithNulls(R.id.carbMinValue);
        Double minFiber = getDoubleFromViewWithNulls(R.id.fiberMinValue);
        Double minProtein = getDoubleFromViewWithNulls(R.id.proteinMinValue);
        return new NutritionFactsFilter(maxFat, maxCarb, maxFiber, maxProtein, minFat,
                minCarb, minFiber, minProtein);
    }

    private void setRecommendedMaxValuesText(String text) {
        setViewText(R.id.recommendedMaxValuesText, text);
    }

    public NutritionFactsFilterDialog(NutritionFactsFilter factsFilter,
                                      CalorieInfo recommendedFacts, boolean isAddMeal) {
        this.factsFilter = factsFilter;
        this.recommendedFacts = recommendedFacts;
        this.isAddMeal = isAddMeal;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = getBuilder();
        setInflaterView(R.layout.nutrition_facts_filter_dialog);
        builder.setView(this.view).setNegativeButton(Utils.CANCEL, null);
        builder.setView(this.view).setPositiveButton(Utils.OK,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Utils.UPDATE_FACTS_FILTER);
                intent.putExtra(Utils.FACTS_FILTER, getFactsFilterFromFields());
                Objects.requireNonNull(getContext()).sendBroadcast(intent);
            }
        });
        populateFields();
        return builder.create();
    }

}
