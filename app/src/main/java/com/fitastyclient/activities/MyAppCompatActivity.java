package com.fitastyclient.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.Account;
import com.fitastyclient.data_holders.CalorieInfo;
import com.fitastyclient.data_holders.CountriesObj;
import com.fitastyclient.data_holders.Dish;
import com.fitastyclient.data_holders.Ingredient;
import com.fitastyclient.data_holders.Sensitivities;
import com.fitastyclient.dialogs.DishInfoDialog;
import com.fitastyclient.dialogs.IngredientInfoDialog;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MyAppCompatActivity extends AppCompatActivity {

    public static String typeString = "Type";
    public static String nameString = "Name";
    public static String amountString = "Amount";
    public static String requiredField = "This field is required.";
    public static String ingredientType = "Ing";
    public static String dishType = "Dish";
    public static String createText = "Create";
    public static String addText = "Add";
    public static String editText = "Edit";

    public static double tableWidthPercent = 0.9;

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

    protected void setViewTextAndColor(int viewId, String text, int colorId) {
        TextView view = findViewById(viewId);
        view.setTextColor(getResources().getColor(colorId));
        view.setText(text);
    }

    protected void displayError(int viewId, String text) {
        setViewTextAndColor(viewId, text, R.color.red);
    }

    protected void displayFieldIsRequired(int viewId) {
        displayError(viewId, requiredField);
    }

    protected void displayActionFailed(int viewId) {
        displayError(viewId, Utils.ACTION_FAILED);
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

    protected void setCheckBox(int checkBoxId, boolean checked) {
        ((CheckBox) findViewById(checkBoxId)).setChecked(checked);
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

    protected int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
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
                                    int leftPadding, boolean toCenter,
                                    int heightGaps, int heightFactor, int textSize) {
        TextView view = new TextView(this);
        viewHeight += (heightGaps * heightFactor);
        view.setLayoutParams(new TableRow.LayoutParams(viewWidth, viewHeight, viewWeight));
        if (toCenter) view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(leftPadding, 0, 0, 0);
        if (!text.isEmpty()) {
            if (textColorId != 0) view.setTextColor(getColorById(textColorId));
            if (textSize != 0) view.setTextSize(textSize);
            view.setText(text);
        }
        row.addView(view);
    }

    protected void setViewWithTempFiller(int viewId) {
        setViewText(viewId, Utils.TEMP_FILLER);
    }

    private int getCalorieInfoTableTextColor(boolean toColor, double baseValue, double value,
                                             int defaultColorId) {
        if (toColor) {
            double greenThreshold = baseValue * Utils.GREEN_THRESHOLD_PERCENT;
            double grayThreshold = baseValue * Utils.GRAY_THRESHOLD_PERCENT;
            if (Utils.isValueInThresholdRange(value, greenThreshold)) return R.color.green;
            if (Utils.isValueInThresholdRange(value, grayThreshold)) return defaultColorId;
            return R.color.red;
        } else {
            return defaultColorId;
        }
    }

    private void setViewWithValue(int viewId, double baseValue, double value, boolean toColor,
                                  int defaultColorId, String text) {
        setViewWithTempFiller(viewId);
        int colorId = getCalorieInfoTableTextColor(toColor, baseValue, value, defaultColorId);
        setViewTextAndColor(viewId, text, colorId);
    }

    private void setViewWithCalorie(int viewId, double baseValue, double value, boolean toColor) {
        String text = Utils.cleanDoubleToString(value);
        setViewWithValue(viewId, baseValue, value, toColor, R.color.black, text);
    }

    private void setViewWithFact(int viewId, double baseValue, double value, boolean toColor) {
        String text = Utils.cleanDoubleToString(value) + Utils.GRAM;
        setViewWithValue(viewId, baseValue, value, toColor, R.color.gray, text);
    }

    protected Intent getIntentWithBooleanFlag(Context fromContext, Class<?> toClass,
                                              String flagText, boolean flagValue) {
        Intent intent = new Intent(fromContext, toClass);
        Bundle bundle = new Bundle();
        bundle.putBoolean(flagText, flagValue);
        intent.putExtras(bundle);
        return intent;
    }

    protected ImageView getImageView(int icon, int size, int colorId, int leftPadding) {
        ImageView view = new ImageView(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(size, size);
        layoutParams.gravity = Gravity.CENTER;
        view.setLayoutParams(layoutParams);
        view.setImageResource(icon);
        view.setPadding(leftPadding, 0, 0, 0);
        if (colorId != 0) {
            view.setColorFilter(getColorById(colorId), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        return view;
    }

    protected void addEmptyTextViewToRow(TableRow row, int width) {
        addTextViewToRow(row, Utils.EMPTY, R.color.black, width, 0, 0, 0, true, 0, 0, 0);
    }

    protected void deleteItemFromTable(View view) {
        View row = (View) view.getParent();
        ViewGroup container = ((ViewGroup)row.getParent());
        container.removeView(row);
        container.invalidate();
    }

    protected void setViewVisibility(int viewId, int visibility) {
        findViewById(viewId).setVisibility(visibility);
    }

    protected void makeViewVisible(int viewId) {
        setViewVisibility(viewId, View.VISIBLE);
    }

    protected void makeViewInvisible(int viewId) {
        setViewVisibility(viewId, View.INVISIBLE);
    }

    protected void makeViewGone(int viewId) {
        setViewVisibility(viewId, View.GONE);
    }

    protected void setViewHeight(int viewId, int height) {
        findViewById(viewId).getLayoutParams().height = height;
    }

    protected void setViewWidth(int viewId, int width) {
        findViewById(viewId).getLayoutParams().width = width;
    }

    protected void displayAlertPopup(String titleText, String bodyText, int iconColorId,
                                     DialogInterface.OnClickListener onClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setTitle(titleText)
                .setMessage(bodyText)
                .setPositiveButton(Utils.YES, onClickListener)
                .setNegativeButton(Utils.NO, null)
                .setIcon(android.R.drawable.stat_notify_error)
                .show();
        ((ImageView) Objects.requireNonNull(dialog.findViewById(android.R.id.icon)))
                .setColorFilter(getColorById(iconColorId),
                        android.graphics.PorterDuff.Mode.SRC_IN);
    }
    protected void addInfoButtonToRow(TableRow row, final String itemName,
                                      final boolean isIngredient, final int infoTextId,
                                      boolean disableInfoButton) {
        int infoIconSize = 90;
        int infoIconLeftPadding = 10;
        ImageView view = getImageView(android.R.drawable.ic_dialog_info,
                infoIconSize, R.color.blue, infoIconLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemInfo(itemName, isIngredient, infoTextId);
            }
        });
        if (disableInfoButton) view.setVisibility(View.INVISIBLE);
        row.addView(view);
    }

    private <T> void sendGetItemInfoRequest(Call<ResponseBody> call, final boolean isIngredient,
                                            final int infoTextId, final Class<T> itemClass) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    T item = Utils.getResponseObject(response, itemClass);
                    if (item != null) {
                        clearInformationText(infoTextId);
                        if (isIngredient) displayIngredientInfoDialog((Ingredient) item);
                        else displayDishInfoDialog((Dish) item);
                    } else {
                        displayActionFailed(infoTextId);
                    }
                } else {
                    displayActionFailed(infoTextId);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                displayActionFailed(infoTextId);
            }
        });
    }

    protected void getIngredientInfo(String ingredientName, int infoTextId) {
        sendGetItemInfoRequest(Utils.getRetrofitApi()
                .getIngredientInfo(ingredientName), true, infoTextId, Ingredient.class);
    }

    protected void getDishInfo(String dishName, int infoTextId) {
        sendGetItemInfoRequest(Utils.getRetrofitApi()
                .getDishInfo(dishName), false, infoTextId, Dish.class);
    }

    protected void getItemInfo(String itemName, boolean isIngredient, int infoTextId) {
        if (isIngredient) getIngredientInfo(itemName, infoTextId);
        else getDishInfo(itemName, infoTextId);
    }

    protected void updateCalorieInfoTable(CalorieInfo recommended, CalorieInfo current,
                                          CalorieInfo difference,
            int recommendedCaloriesId, int currentCaloriesId, int CaloriesDifferenceId,
            int recommendedFatId, int currentFatId, int fatDifferenceId,
            int recommendedCarbId, int currentCarbId, int carbDifferenceId,
            int recommendedFiberId, int currentFiberId, int fiberDifferenceId,
            int recommendedProteinId, int currentProteinId, int proteinDifferenceId) {

        double carbRecommended = recommended.getCarb();
        double fiberRecommended = recommended.getFiber();
        double fatRecommended = recommended.getFat();
        double proteinRecommended = recommended.getProtein();
        double caloriesRecommended = Utils.getCalories(fatRecommended, carbRecommended,
                proteinRecommended);

        double currentCarb = current.getCarb();
        double currentFiber = current.getFiber();
        double currentFat = current.getFat();
        double currentProtein = current.getProtein();
        double currentCalories = Utils.getCalories(currentFat, currentCarb, currentProtein);

        double carbDifference = carbRecommended - currentCarb;
        double fiberDifference = fiberRecommended - currentFiber;
        double fatDifference = fatRecommended - currentFat;
        double proteinDifference = proteinRecommended - currentProtein;
        double caloriesDifference = caloriesRecommended - currentCalories;

        difference.setCarb(carbDifference);
        difference.setFiber(fiberDifference);
        difference.setFat(fatDifference);
        difference.setProtein(proteinDifference);

        setViewWithCalorie(recommendedCaloriesId, 0, caloriesRecommended, false);
        setViewWithCalorie(currentCaloriesId, 0, currentCalories, false);
        setViewWithCalorie(CaloriesDifferenceId, caloriesRecommended, caloriesDifference, true);

        setViewWithFact(recommendedFatId, 0, fatRecommended, false);
        setViewWithFact(currentFatId, 0, currentFat, false);
        setViewWithFact(fatDifferenceId, fatRecommended, fatDifference, true);

        setViewWithFact(recommendedCarbId, 0, carbRecommended, false);
        setViewWithFact(currentCarbId, 0, currentCarb, false);
        setViewWithFact(carbDifferenceId, carbRecommended, carbDifference, true);

        setViewWithFact(recommendedFiberId, 0, fiberRecommended, false);
        setViewWithFact(currentFiberId, 0, currentFiber, false);
        setViewWithFact(fiberDifferenceId, fiberRecommended, fiberDifference, true);

        setViewWithFact(recommendedProteinId, 0, proteinRecommended, false);
        setViewWithFact(currentProteinId, 0, currentProtein, false);
        setViewWithFact(proteinDifferenceId, proteinRecommended, proteinDifference, true);
    }

    protected void startCreateEditAccountActivity(Context fromContext, CountriesObj countriesObj,
                                                  boolean isCreateNew, Account account) {
        Intent intent = getIntentWithBooleanFlag(fromContext, AccountActivity.class,
                Utils.IS_CREATE_NEW_ACCOUNT, isCreateNew);
        intent.putExtra(Utils.COUNTRIES_OBJ, countriesObj);
        if (!isCreateNew) {
            intent.putExtra(Utils.ACCOUNT, account);
        }
        startActivity(intent);
    }

    protected void tryToCreateEditAccount(final Context fromContext, final boolean isCreateNew,
                                          final int errorInfoTextId, final Account account) {
        Utils.getRetrofitApi().getCountries()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            CountriesObj countriesObj = Utils.getResponseObject(
                                    response, CountriesObj.class);
                            if (countriesObj != null) {
                                startCreateEditAccountActivity(fromContext, countriesObj,
                                        isCreateNew, account);
                            } else {
                                displayActionFailed(errorInfoTextId);
                            }
                        } else {
                            displayActionFailed(errorInfoTextId);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayActionFailed(errorInfoTextId);
                    }
                });
    }

    protected void startSearchItemsActivity(Context fromContext, String username,
                                            boolean isForMeal, Sensitivities sensitivities,
                                            CalorieInfo differenceCalorieInfo) {
        Intent intent = getIntentWithBooleanFlag(fromContext, SearchItemsActivity.class,
                Utils.IS_FOR_MEAL, isForMeal);
        Bundle bundle = new Bundle();
        bundle.putString(Utils.USERNAME, username);
        intent.putExtras(bundle);
        intent.putExtra(Utils.SENSITIVITIES, sensitivities);
        if (isForMeal) {
            intent.putExtra(Utils.CALORIE_INFO, differenceCalorieInfo);
        }
        startActivity(intent);
    }

    protected void tryToStartSearchItemsActivity(final String username, final Context fromContext,
                                                 final boolean isForMeal,
                                                 final int errorInfoTextId,
                                                 final CalorieInfo differenceCalorieInfo) {
        Utils.getRetrofitApi().getAccountInformation(username)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Account account = Utils.getResponseObject(response, Account.class);
                            if (account != null) {
                                Sensitivities sensitivities = account.getSensitivitiesFromAccount();
                                startSearchItemsActivity(fromContext, username, isForMeal,
                                        sensitivities, differenceCalorieInfo);
                            } else {
                                displayActionFailed(errorInfoTextId);
                            }
                        } else {
                            displayActionFailed(errorInfoTextId);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayActionFailed(errorInfoTextId);
                    }
                });
    }
}
