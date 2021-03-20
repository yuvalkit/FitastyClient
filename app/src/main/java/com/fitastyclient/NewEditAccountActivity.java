package com.fitastyclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewEditAccountActivity extends AppCompatActivity {

    static public String createNewAccount = "Create New Account";
    static public String editAccount = "Edit Account";
    static public String createText = "Create";
    static public String editText = "Edit";
    static public String requiredField = "This field is required.";
    static public String usernameAlreadyTaken = "This username is already taken!";
    static public String usernameIsAvailable = "This username is available!";
    static public String thisIsYourUsername = "This is your current username.";
    static public String usernameCheckFailed = "Check failed, please try again.";
    static public String accountCreationFailed = "Account creation failed, please try again.";
    static public String accountEditFailed = "Account edit failed, please try again.";
    static public String currentAccountDetails = "This is your current account details.";
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

    private View.OnClickListener cancelButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener checkUsernameButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            checkUsername();
        }
    };

    private View.OnClickListener createEditButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearInformationText(R.id.createEditInformationText);
            if (checkAllFields()) {
                if (isCreateNew) createNewAccount();
                else editAccount();
            }
        }
    };

    private void setViewText(int viewId, String text) {
        ((TextView) findViewById(viewId)).setText(text);
    }

    private void setViewColorAndText(int viewId, String text, int colorName) {
        TextView view = findViewById(viewId);
        view.setTextColor(getResources().getColor(colorName));
        view.setText(text);
    }

    private void displayMustEnterField(int textViewId) {
        setViewColorAndText(textViewId, requiredField, R.color.red);
    }

    private void clearInformationText(int textViewId) {
        setViewText(textViewId, Utils.EMPTY);
    }

    private void setUsernameInformationText(String text, int colorName) {
        setViewColorAndText(R.id.usernameInformationText, text, colorName);
    }

    private void displayUsernameAlreadyInUse() {
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
        displayMustEnterField(R.id.usernameInformationText);
    }

    private void displayAccountCreationFailed() {
        setViewColorAndText(R.id.createEditInformationText, accountCreationFailed, R.color.red);
    }

    private void displayAccountEditFailed() {
        setViewColorAndText(R.id.createEditInformationText, accountEditFailed, R.color.red);
    }

    private void displayCurrentAccountDetails() {
        setViewColorAndText(R.id.createEditInformationText, currentAccountDetails, R.color.red);
    }

    private String getTextFromView(int viewId) {
        EditText editText = findViewById(viewId);
        return editText.getText().toString();
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

    private void toggleRadioButton(int radioButtonId) {
        ((RadioButton) findViewById(radioButtonId)).toggle();
    }

    private boolean isRadioButtonChecked(int radioButtonId) {
        return ((RadioButton) findViewById(radioButtonId)).isChecked();
    }

    private boolean isRadioButtonsEmpty() {
        int genderInfoId = R.id.genderInformationText;
        if (!isRadioButtonChecked(R.id.maleRadioButton) &&
                !isRadioButtonChecked(R.id.femaleRadioButton)) {
            displayMustEnterField(genderInfoId);
            return true;
        }
        clearInformationText(genderInfoId);
        return false;
    }

    boolean isFieldEmpty(String text, int InfoId) {
        if (text.isEmpty()) {
            displayMustEnterField(InfoId);
            return true;
        }
        clearInformationText(InfoId);
        return false;
    }

    private boolean checkAllFields() {
        boolean valid = true;
        int passwordInfoId = R.id.passwordInformationText;
        int ageInfoId = R.id.ageInformationText;
        int heightInfoId = R.id.heightInformationText;
        int weightInfoId = R.id.weightInformationText;
        String username = getUsername();
        String password = getPassword();
        String age = getAge();
        String height = getHeight();
        String weight = getWeight();
        if (isUsernameEmpty(username)) valid = false;
        if (isFieldEmpty(password, passwordInfoId)) valid = false;
        if (isRadioButtonsEmpty()) valid = false;
        if (isFieldEmpty(age, ageInfoId)) valid = false;
        if (isFieldEmpty(height, heightInfoId)) valid = false;
        if (isFieldEmpty(weight, weightInfoId)) valid = false;
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
        HttpManager.getRetrofitApi().isUsernameAvailable(username)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                assert response.body() != null;
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                boolean exist = jsonObject.getBoolean(Utils.USERNAME_EXIST);
                                if (!exist) {
                                    displayUsernameAvailable();
                                } else {
                                    displayUsernameAlreadyInUse();
                                }
                            } catch (Exception e) {
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

    private void createNewAccount() {
        final Account account = getAccountFromFields();
        HttpManager.getRetrofitApi().insertNewAccount(account)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                assert response.body() != null;
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                boolean exist = jsonObject.getBoolean(Utils.USERNAME_EXIST);
                                if (!exist) {
                                    Utils.displayToast(getApplicationContext(), accountCreated);
                                    startMainMenuActivity(account.getUsername());
                                    finish();
                                } else {
                                    displayUsernameAlreadyInUse();
                                }
                            } catch (Exception e) {
                                displayAccountCreationFailed();
                            }
                        } else {
                            displayAccountCreationFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayAccountCreationFailed();
                    }
                });
    }

    private void editAccount() {
        final Account account = getAccountFromFields();
        if (account.equals(this.account)) {
            displayCurrentAccountDetails();
            clearInformationText(R.id.usernameInformationText);
            return;
        }
        HttpManager.getRetrofitApi().updateAccount(this.account.getUsername(), account)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                assert response.body() != null;
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                boolean exist = jsonObject.getBoolean(Utils.USERNAME_EXIST);
                                if (!exist) {
                                    sendBroadcast(new Intent(Utils.FINISH_MAIN_MENU_ACTIVITY));
                                    Utils.displayToast(getApplicationContext(), accountEdited);
                                    startMainMenuActivity(account.getUsername());
                                    finish();
                                } else {
                                    displayUsernameAlreadyInUse();
                                }
                            } catch (Exception e) {
                                displayAccountEditFailed();
                            }
                        } else {
                            displayAccountEditFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayAccountEditFailed();
                    }
                });
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

    private void displayTitleText() {
        if (this.isCreateNew) setTitleText(createNewAccount);
        else setTitleText(editAccount);
    }

    private void displayButtonText() {
        if (this.isCreateNew) setButtonText(createText);
        else setButtonText(editText);
    }

    private void setSpinner(Spinner spinner, int stringsArrayId, int selection_index) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                stringsArrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selection_index);
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
                .getBoolean(Utils.IS_CREATE_NEW);
        displayTitleText();
        displayButtonText();
        if (!this.isCreateNew) {
            this.account = (Account) getIntent().getSerializableExtra(Utils.ACCOUNT);
            populateFieldsFromAccount();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit_account_layout);
        setComponents();
    }
}
