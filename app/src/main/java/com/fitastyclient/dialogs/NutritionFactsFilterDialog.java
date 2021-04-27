package com.fitastyclient.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.CalorieInfo;
import com.fitastyclient.data_holders.NutritionFactsFilter;
import java.util.Objects;

public class NutritionFactsFilterDialog extends MyDialogFragment {

    public static String recommendedFactValues = "These facts values are recommended for you.";

    private NutritionFactsFilter factsFilter;
    private CalorieInfo recommendedFacts;
    private EditText fatEditText;
    private EditText carbEditText;
    private EditText fiberEditText;
    private EditText proteinEditText;
    private TextWatcher textWatcher;
    private boolean isInfoActive;

    private View.OnClickListener infoButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (isInfoActive) {
                changeInfoTextsVisibility(View.GONE);
            } else {
                changeInfoTextsVisibility(View.VISIBLE);
            }
            isInfoActive = !isInfoActive;
        }
    };

    private View.OnClickListener resetButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            setFactsFields(new NutritionFactsFilter(recommendedFacts,
                    factsFilter.getMinPercent(), factsFilter.getMaxPercent()), false);
            setRecommendedValuesText(recommendedFactValues);
            setTextChangedListeners();
        }
    };

    private void changeInfoTextsVisibility(int visibility) {
        changeViewVisibility(R.id.factFieldsInfoText, visibility);
        changeViewVisibility(R.id.filterIsPerServingText, visibility);
        changeViewVisibility(R.id.minMaxAmountInfoText, visibility);
        changeViewVisibility(R.id.leaveEmptyForNoFilterText, visibility);
    }

    private void setViewDoubleWithNulls(int viewId, Double value) {
        if (value != null) {
            setViewText(viewId, Utils.cleanDoubleToString(value));
        } else {
            setViewText(viewId, Utils.EMPTY);
        }
    }

    private void setViewPercentWithNulls(int viewId, Double percent) {
        if (percent != null) setViewDoubleWithNulls(viewId, percent * Utils.PERCENT_SCALE);
        else setViewDoubleWithNulls(viewId, null);
    }

    private void setFactsFields(NutritionFactsFilter filter, boolean setAmountFields) {
        setViewDoubleWithNulls(R.id.nutritionFactsFilterFatValue, filter.getFat());
        setViewDoubleWithNulls(R.id.nutritionFactsFilterCarbValue, filter.getCarb());
        setViewDoubleWithNulls(R.id.nutritionFactsFilterFiberValue, filter.getFiber());
        setViewDoubleWithNulls(R.id.nutritionFactsFilterProteinValue, filter.getProtein());
        if (setAmountFields) {
            setViewPercentWithNulls(R.id.nutritionFactsFilterMinAmountValue,
                    filter.getMinPercent());
            setViewPercentWithNulls(R.id.nutritionFactsFilterMaxAmountValue,
                    filter.getMaxPercent());
        }
    }

    private void populateFields() {
        setFactsFields(this.factsFilter, true);
        this.isInfoActive = false;
        this.fatEditText = this.view.findViewById(R.id.nutritionFactsFilterFatValue);
        this.carbEditText = this.view.findViewById(R.id.nutritionFactsFilterCarbValue);
        this.fiberEditText = this.view.findViewById(R.id.nutritionFactsFilterFiberValue);
        this.proteinEditText = this.view.findViewById(R.id.nutritionFactsFilterProteinValue);
        this.textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setRecommendedValuesText(Utils.EMPTY);
                removeTextChangedListeners();
            }};
        this.view.findViewById(R.id.nutritionFactsFilterInfoButton)
                .setOnClickListener(this.infoButtonClick);
        changeInfoTextsVisibility(View.GONE);
        this.view.findViewById(R.id.resetToRecommendedButton)
                .setOnClickListener(this.resetButtonClick);
        if (isFactsFilterHasRecommendedFacts()) {
            setRecommendedValuesText(recommendedFactValues);
        }
        setTextChangedListeners();
    }

    private boolean isFactsFilterHasRecommendedFacts() {
        Double fat = this.factsFilter.getFat();
        Double carb = this.factsFilter.getCarb();
        Double fiber = this.factsFilter.getFiber();
        Double protein = this.factsFilter.getProtein();
        if ((fat == null) || (carb == null) || (fiber == null) || (protein == null)) {
            return false;
        }
        return (Utils.roundTwo(fat) == Utils.roundTwo(this.recommendedFacts.getFat()))
                && (Utils.roundTwo(carb) == Utils.roundTwo(this.recommendedFacts.getCarb()))
                && (Utils.roundTwo(fiber) == Utils.roundTwo(this.recommendedFacts.getFiber()))
                && (Utils.roundTwo(protein) == Utils.roundTwo(this.recommendedFacts.getProtein()));
    }

    private void setTextChangedListeners() {
        setMaxValueTextChanged(this.fatEditText);
        setMaxValueTextChanged(this.carbEditText);
        setMaxValueTextChanged(this.fiberEditText);
        setMaxValueTextChanged(this.proteinEditText);
    }

    private void removeTextChangedListeners() {
        removeMaxValueTextChanged(this.fatEditText);
        removeMaxValueTextChanged(this.carbEditText);
        removeMaxValueTextChanged(this.fiberEditText);
        removeMaxValueTextChanged(this.proteinEditText);
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
        return (!Utils.isEmptyNumber(text)) ? Double.parseDouble(text) : null;
    }

    private Double getDoubleFromViewWithNulls(int viewId) {
        return getDoubleFromTextWithNulls(getTextFromView(viewId));
    }

    private Double getPercentFromViewWithNulls(int viewId) {
        Double percent = getDoubleFromViewWithNulls(viewId);
        return (percent != null) ? (percent / Utils.PERCENT_SCALE) : null;
    }

    private NutritionFactsFilter getFactsFilterFromFields() {
        Double fat = getDoubleFromViewWithNulls(R.id.nutritionFactsFilterFatValue);
        Double carb = getDoubleFromViewWithNulls(R.id.nutritionFactsFilterCarbValue);
        Double fiber = getDoubleFromViewWithNulls(R.id.nutritionFactsFilterFiberValue);
        Double protein = getDoubleFromViewWithNulls(R.id.nutritionFactsFilterProteinValue);
        Double minPercent = getPercentFromViewWithNulls(R.id.nutritionFactsFilterMinAmountValue);
        Double maxPercent = getPercentFromViewWithNulls(R.id.nutritionFactsFilterMaxAmountValue);
        return new NutritionFactsFilter(fat, carb, fiber, protein, minPercent, maxPercent);
    }

    private void setRecommendedValuesText(String text) {
        setViewText(R.id.recommendedValuesText, text);
    }

    private View.OnClickListener getEraseButtonListener(final int inputBarId) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                clearViewText(inputBarId);
            }
        };
    }

    private void setEraseButtons() {
        this.view.findViewById(R.id.fatInputEraseButton).setOnClickListener(
                getEraseButtonListener(R.id.nutritionFactsFilterFatValue));
        this.view.findViewById(R.id.carbInputEraseButton).setOnClickListener(
                getEraseButtonListener(R.id.nutritionFactsFilterCarbValue));
        this.view.findViewById(R.id.fiberInputEraseButton).setOnClickListener(
                getEraseButtonListener(R.id.nutritionFactsFilterFiberValue));
        this.view.findViewById(R.id.proteinInputEraseButton).setOnClickListener(
                getEraseButtonListener(R.id.nutritionFactsFilterProteinValue));
        this.view.findViewById(R.id.minAmountInputEraseButton).setOnClickListener(
                getEraseButtonListener(R.id.nutritionFactsFilterMinAmountValue));
        this.view.findViewById(R.id.maxAmountInputEraseButton).setOnClickListener(
                getEraseButtonListener(R.id.nutritionFactsFilterMaxAmountValue));
    }

    public NutritionFactsFilterDialog(NutritionFactsFilter factsFilter,
                                      CalorieInfo recommendedFacts) {
        this.factsFilter = factsFilter;
        this.recommendedFacts = recommendedFacts;
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
        setEraseButtons();
        return builder.create();
    }

}
