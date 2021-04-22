package com.fitastyclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import com.fitastyclient.data_holders.Account;
import com.fitastyclient.data_holders.DietType;
import com.fitastyclient.data_holders.NameExistObj;
import com.fitastyclient.R;
import com.fitastyclient.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends MyAppCompatActivity {

    static public String createNewAccount = "Create New Account";
    static public String editAccount = "Edit Account";
    static public String usernameAlreadyTaken = "This username is already taken!";
    static public String usernameIsAvailable = "This username is available!";
    static public String thisIsYourUsername = "This is your current username.";
    static public String usernameCheckFailed = "Check failed, please try again.";
    static public String accountCreationFailed = "Account creation failed, please try again.";
    static public String accountEditFailed = "Account edit failed, please try again.";
    static public String currentAccountDetails = "These are your current account details.";
    static public String accountCreated = "Account Created";
    static public String accountEdited = "Account Edited";

    static public Map<Integer, Double> activityFactorMap = new HashMap<Integer, Double>() {{
        put(0, 1.2);
        put(1, 1.375);
        put(2, 1.55);
        put(3, 1.725);
        put(4, 1.9);
    }};

    static public Map<Integer, DietType> dietTypeMap = new HashMap<Integer, DietType>() {{
        put(0, new DietType(0.2, 0.35, 0.45));
        put(1, new DietType(0.4, 0.3, 0.3));
        put(2, new DietType(0.5, 0.2, 0.3));
    }};

    static public Map<Integer, Double> weightGoalMap = new HashMap<Integer, Double>() {{
        put(0, -1.0);
        put(1, -0.75);
        put(2, -0.5);
        put(3, -0.25);
        put(4, 0.0);
        put(5, 0.25);
        put(6, 0.5);
        put(7, 0.75);
        put(8, 1.0);
    }};

    private boolean isCreateNew;
    private Account account;
    private Spinner activitySpinner;
    private Spinner goalSpinner;
    private Spinner dietTypeSpinner;

    private View.OnClickListener checkUsernameButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            checkUsername();
        }
    };

    private View.OnClickListener createEditButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearInformationText(R.id.createEditInformationText);
            if (checkAllFields()) {
                if (isCreateNew) {
                    createNewAccount();
                }
                else {
                    editAccount();
                }
            }
        }
    };

    private void setUsernameInformationText(String text, int colorName) {
        setViewTextAndColor(R.id.usernameInformationText, text, colorName);
    }

    private void displayUsernameAlreadyTaken() {
        setUsernameInformationText(usernameAlreadyTaken, R.color.red);
    }

    private void displayUsernameAvailable() {
        setUsernameInformationText(usernameIsAvailable, R.color.green);
    }

    private void displayThisIsYourUsername() {
        setUsernameInformationText(thisIsYourUsername, R.color.green);
    }

    private void displayUsernameCheckFailed() {
        setUsernameInformationText(usernameCheckFailed, R.color.red);
    }

    private void displayMustInsertUsername() {
        displayFieldIsRequired(R.id.usernameInformationText);
    }

    private void displayError(String text) {
        setViewTextAndColor(R.id.createEditInformationText, text, R.color.red);
    }

    private void displayCurrentAccountDetails() {
        setViewTextAndColor(R.id.createEditInformationText, currentAccountDetails, R.color.red);
    }

    private String getUsername() {
        return getTextFromView(R.id.newEditAccountUsernameInput);
    }

    private String getPassword() {
        return getTextFromView(R.id.newEditAccountPasswordInput);
    }

    private String getAge() {
        return getTextFromView(R.id.ageInput);
    }

    private String getHeight() {
        return getTextFromView(R.id.heightInput);
    }

    private String getWeight() {
        return getTextFromView(R.id.weightInput);
    }

    private boolean isUsernameEmpty(String username) {
        if (username.isEmpty()) {
            displayMustInsertUsername();
            return true;
        }
        return false;
    }

    private boolean isRadioButtonsEmpty() {
        int genderInfoId = R.id.genderInformationText;
        if (!isRadioButtonChecked(R.id.maleRadioButton) &&
                !isRadioButtonChecked(R.id.femaleRadioButton)) {
            displayFieldIsRequired(genderInfoId);
            return true;
        }
        clearInformationText(genderInfoId);
        return false;
    }

    private boolean checkAllFields() {
        boolean valid = true;
        int passwordViewId = R.id.newEditAccountPasswordInput;
        int ageViewId = R.id.newEditAccountPasswordInput;
        int heightViewId = R.id.newEditAccountPasswordInput;
        int weightViewId = R.id.newEditAccountPasswordInput;
        int passwordInfoId = R.id.passwordInformationText;
        int ageInfoId = R.id.ageInformationText;
        int heightInfoId = R.id.heightInformationText;
        int weightInfoId = R.id.weightInformationText;
        String username = getUsername();
        if (isUsernameEmpty(username)) valid = false;
        if (isFieldEmptyQuery(passwordViewId, passwordInfoId)) valid = false;
        if (isRadioButtonsEmpty()) valid = false;
        if (isFieldEmptyQuery(ageViewId, ageInfoId)) valid = false;
        if (isFieldEmptyQuery(heightViewId, heightInfoId)) valid = false;
        if (isFieldEmptyQuery(weightViewId, weightInfoId)) valid = false;
        return valid;
    }

    private void checkUsername() {
        String username = getUsername();
        if (isUsernameEmpty(username)) {
            return;
        }
        if (!this.isCreateNew && username.equals(this.account.getUsername())) {
            displayThisIsYourUsername();
            return;
        }
        Utils.getRetrofitApi().isUsernameAvailable(username)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            NameExistObj nameExistObj = Utils.getResponseNameExistObj(response);
                            if (nameExistObj != null) {
                                if (nameExistObj.getNameExist()) {
                                    displayUsernameAlreadyTaken();
                                } else {
                                    displayUsernameAvailable();
                                }
                            } else {
                                displayUsernameCheckFailed();
                            }
                        } else {
                            displayUsernameCheckFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayUsernameCheckFailed();
                    }
                });
    }

    private Account getAccountFromFields() {
        String username = getUsername();
        String password = getPassword();
        int age = Integer.parseInt(getAge());
        boolean isMale = isRadioButtonChecked(R.id.maleRadioButton);
        int height = Integer.parseInt(getHeight());
        int weight = Integer.parseInt(getWeight());
        int activitySelection = this.activitySpinner.getSelectedItemPosition();
        Double activityFactorDouble = activityFactorMap.get(activitySelection);
        assert activityFactorDouble != null;
        double activityFactor = activityFactorDouble;
        int dietTypeSelection = this.dietTypeSpinner.getSelectedItemPosition();
        DietType dietType = dietTypeMap.get(dietTypeSelection);
        int goalSelection = this.goalSpinner.getSelectedItemPosition();
        Double weightGoalDouble = weightGoalMap.get(goalSelection);
        assert weightGoalDouble != null;
        double weightGoal = weightGoalDouble;
        return new Account(username, password, age, isMale, height, weight,
                activityFactor, dietType, weightGoal);
    }

    private void startMainMenuActivity(String username) {
        Intent intent = Utils.getIntentWithUsername(this, MainMenuActivity.class, username);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void tryToCreateEditAccount(boolean nameExist, Account account) {
        if (nameExist) {
            displayUsernameAlreadyTaken();
        } else {
            if (!this.isCreateNew) sendBroadcast(new Intent(Utils.FINISH_MAIN_MENU_ACTIVITY));
            String toastText = (this.isCreateNew) ? accountCreated : accountEdited;
            Utils.displayToast(getApplicationContext(), toastText);
            startMainMenuActivity(account.getUsername());
            finish();
        }
    }

    private Callback<ResponseBody> getCreateEditCallback(final Account account,
                                                         final String failedText) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    NameExistObj nameExistObj = Utils.getResponseNameExistObj(response);
                    if (nameExistObj != null) {
                        tryToCreateEditAccount(nameExistObj.getNameExist(), account);
                    } else {
                        displayError(failedText);
                    }
                } else {
                    displayError(failedText);
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,
                                  @NonNull Throwable t) {
                displayError(failedText);
            }
        };
    }

    private void createNewAccount() {
        final Account account = getAccountFromFields();
        Utils.getRetrofitApi().insertNewAccount(account)
                .enqueue(getCreateEditCallback(account, accountCreationFailed));
    }

    private void editAccount() {
        final Account account = getAccountFromFields();
        if (account.equals(this.account)) {
            displayCurrentAccountDetails();
            clearInformationText(R.id.usernameInformationText);
            return;
        }
        Utils.getRetrofitApi().updateAccount(this.account.getUsername(), account)
                .enqueue(getCreateEditCallback(account, accountEditFailed));
    }

    private <V> void setSpinnerByKey(Map<Integer, V> map, V value, Spinner spinner) {
        Integer key = Utils.getKeyByValue(map, value);
        if (key != null) spinner.setSelection(key);
    }

    private void populateFieldsFromAccount() {
        setViewText(R.id.newEditAccountUsernameInput, this.account.getUsername());
        setViewText(R.id.newEditAccountPasswordInput, this.account.getPassword());
        if (this.account.getIsMale()) toggleRadioButton(R.id.maleRadioButton);
        else toggleRadioButton(R.id.femaleRadioButton);
        setViewText(R.id.ageInput, Integer.toString(this.account.getAge()));
        setViewText(R.id.heightInput, Integer.toString(this.account.getHeight()));
        setViewText(R.id.weightInput, Integer.toString(this.account.getWeight()));
        setSpinnerByKey(activityFactorMap, this.account.getActivityFactor(), this.activitySpinner);
        setSpinnerByKey(weightGoalMap, this.account.getWeightGoal(), this.goalSpinner);
        setSpinnerByKey(dietTypeMap, this.account.getDietType(), this.dietTypeSpinner);
    }

    private void setTitleText(String text) {
        setViewText(R.id.newEditAccountTitleText, text);
    }

    private void setButtonText(String text) {
        setViewText(R.id.createEditButton, text);
    }

    private void setComponents() {
        this.activitySpinner = findViewById(R.id.activity_spinner);
        this.goalSpinner = findViewById(R.id.goal_spinner);
        this.dietTypeSpinner = findViewById(R.id.diet_type_spinner);
        setSpinner(this.activitySpinner, R.array.activity_spinner_array, 2);
        setSpinner(this.goalSpinner, R.array.goal_spinner_array, 4);
        setSpinner(this.dietTypeSpinner, R.array.diet_type_spinner_array, 1);
        findViewById(R.id.newEditAccountCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.checkUsernameButton).setOnClickListener(this.checkUsernameButtonClick);
        findViewById(R.id.createEditButton).setOnClickListener(this.createEditButtonClick);
        this.isCreateNew = Objects.requireNonNull(getIntent().getExtras())
                .getBoolean(Utils.IS_CREATE_NEW_ACCOUNT);
        setTitleText((this.isCreateNew) ? createNewAccount : editAccount);
        setButtonText((this.isCreateNew) ? createText : editText);
        if (!this.isCreateNew) {
            this.account = (Account) getIntent().getSerializableExtra(Utils.ACCOUNT);
            populateFieldsFromAccount();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_layout);
        setComponents();
    }
}
