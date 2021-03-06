package com.fitastyclient.activities;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.fitastyclient.data_holders.NameExistObj;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddIngredientActivity extends MyAppCompatActivity {

    public static String requiredFields = "These fields are required.";
    public static String maximumSum = "These fields maximum sum is 100g.";
    public static String ingredientAdded = "Ingredient Added";
    public static String ingredientAdditionFailed = "Failed adding ingredient, please try again.";
    public static String nameAlreadyUsed = "There is already an ingredient with this name.";
    public static int maxFactsSum = 100;

    private View.OnClickListener addButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearInformationText(R.id.addIngredientButtonInfoText);
            if (checkAllFields()) {
                addIngredient();
            }
        }
    };

    private View.OnClickListener solidRadioButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            setUnitsTexts(Utils.GRAM);
        }
    };

    private View.OnClickListener liquidRadioButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            setUnitsTexts(Utils.ML);
        }
    };

    private void setUnitsTexts(String units) {
        setViewText(R.id.servingSizeUnits, units);
        setViewText(R.id.factsPer100Text, Utils.FACTS_PER_100_PREFIX + units + Utils.COLON);
    }

    private void displayNameAlreadyUsed() {
        displayError(R.id.ingredientNameInformationText, nameAlreadyUsed);
    }

    private void displayFactsInfoError(String text) {
        displayError(R.id.factsInformationText, text);
    }

    private void displayAdditionFailed() {
        displayError(R.id.addIngredientButtonInfoText, ingredientAdditionFailed);
    }

    private boolean isFactEmpty(String fact) {
        return fact.isEmpty() || fact.equals(Utils.DOT);
    }

    private boolean isNutritionFactsInvalid() {
        String fat = getTextFromView(R.id.fatValueInput);
        String carb = getTextFromView(R.id.carbValueInput);
        String fiber = getTextFromView(R.id.fiberValueInput);
        String protein = getTextFromView(R.id.proteinValueInput);
        if (isFactEmpty(fat) || isFactEmpty(carb) || isFactEmpty(fiber) || isFactEmpty(protein)) {
            displayFactsInfoError(requiredFields);
            return true;
        }
        double fatValue = Double.parseDouble(fat);
        double carbValue = Double.parseDouble(carb);
        double fiberValue = Double.parseDouble(fiber);
        double proteinValue = Double.parseDouble(protein);
        if ((fatValue + carbValue + fiberValue + proteinValue) > maxFactsSum) {
            displayFactsInfoError(maximumSum);
            return true;
        }
        clearInformationText(R.id.factsInformationText);
        return false;
    }

    private boolean checkAllFields() {
        boolean valid = true;
        int nameViewId = R.id.ingredientNameInput;
        int servingSizeViewId = R.id.servingSizeInput;
        int nameInfoId = R.id.ingredientNameInformationText;
        int servingSizeInfoId = R.id.servingSizeInformationText;
        if (isFieldEmptyQuery(nameViewId, nameInfoId)) valid = false;
        if (isFieldEmptyQuery(servingSizeViewId, servingSizeInfoId)) valid = false;
        if (isNutritionFactsInvalid()) valid = false;
        return valid;
    }

    private Ingredient getIngredientFromFields() {
        String name = getTextFromView(R.id.ingredientNameInput);
        boolean isLiquid = isRadioButtonChecked(R.id.liquidRadioButton);
        double fat = Double.parseDouble(getTextFromView(R.id.fatValueInput));
        double carb = Double.parseDouble(getTextFromView(R.id.carbValueInput));
        double fiber = Double.parseDouble(getTextFromView(R.id.fiberValueInput));
        double protein = Double.parseDouble(getTextFromView(R.id.proteinValueInput));
        boolean isVegan = isCheckBoxChecked(R.id.veganCheckBox);
        boolean isVegetarian = isCheckBoxChecked(R.id.vegetarianCheckBox);
        boolean isGlutenFree = isCheckBoxChecked(R.id.glutenFreeCheckBox);
        boolean isLactoseFree = isCheckBoxChecked(R.id.lactoseFreeCheckBox);
        double serving = Double.parseDouble(getTextFromView(R.id.servingSizeInput));
        return new Ingredient(name, isLiquid, fat, carb, fiber, protein, isVegan,
                isVegetarian, isGlutenFree, isLactoseFree, serving);
    }

    private void addIngredient() {
        Ingredient ingredient = getIngredientFromFields();
        Utils.getRetrofitApi().insertNewIngredient(ingredient)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            NameExistObj nameExistObj = Utils.getResponseNameExistObj(response);
                            if (nameExistObj != null) {
                                if (nameExistObj.getNameExist()) {
                                    displayNameAlreadyUsed();
                                } else {
                                    Utils.displayToast(getApplicationContext(), ingredientAdded);
                                    finish();
                                }
                            } else {
                                displayAdditionFailed();
                            }
                        } else {
                            displayAdditionFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayAdditionFailed();
                    }
                });
    }

    private void setComponents() {
        findViewById(R.id.addIngredientCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.addNewIngredientButton).setOnClickListener(this.addButtonClick);
        findViewById(R.id.solidRadioButton).setOnClickListener(this.solidRadioButtonClick);
        findViewById(R.id.liquidRadioButton).setOnClickListener(this.liquidRadioButtonClick);
        setUnitsTexts(Utils.GRAM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient_layout);
        setComponents();
    }
}
