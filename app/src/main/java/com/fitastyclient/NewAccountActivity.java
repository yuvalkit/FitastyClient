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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAccountActivity extends AppCompatActivity {

    static public String requiredField = "This field is required.";
    static public String usernameAlreadyTaken = "This username is already taken!";
    static public String usernameIsAvailable = "This username is available!";
    static public String usernameCheckFailed = "Check failed, please try again.";
    static public String accountCreationFailed = "Account creation failed, please try again.";
    static public String accountCreated = "Account Created";

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

    private View.OnClickListener createButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (checkAllFields()) {
                createNewAccount();
            }
        }
    };

    private void setInformationText(int textViewId, String text, int colorName) {
        TextView view = findViewById(textViewId);
        if (colorName != 0) view.setTextColor(getResources().getColor(colorName));
        view.setText(text);
    }

    private void displayMustEnterField(int textViewId) {
        setInformationText(textViewId, requiredField, R.color.red);
    }

    private void clearInformationText(int textViewId) {
        setInformationText(textViewId, Utils.EMPTY, 0);
    }

    private void setUsernameInformationText(String text, int colorName) {
        setInformationText(R.id.usernameInformationText, text, colorName);
    }

    private void displayUsernameAlreadyInUse() {
        setUsernameInformationText(usernameAlreadyTaken, R.color.red);
    }

    private void displayUsernameAvailable() {
        setUsernameInformationText(usernameIsAvailable, R.color.green);
    }

    private void displayUsernameCheckFailed() {
        setUsernameInformationText(usernameCheckFailed, R.color.red);
    }

    private void displayMustInsertUsername() {
        displayMustEnterField(R.id.usernameInformationText);
    }

    private void displayAccountCreationFailed() {
        setInformationText(R.id.createInformationText, accountCreationFailed, R.color.red);
    }

    private String getTextFromView(int viewId) {
        EditText editText = findViewById(viewId);
        return editText.getText().toString();
    }

    private String getUsername() {
        return getTextFromView(R.id.newAccountUsernameInput);
    }

    private String getPassword() {
        return getTextFromView(R.id.newAccountPasswordInput);
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

    private boolean isRadioButtonChecked(int radioButtonId) {
        return ((RadioButton) findViewById(radioButtonId)).isChecked();
    }

    boolean isFieldEmpty(String text, int InfoId) {
        if (text.isEmpty()) {
            displayMustEnterField(InfoId);
            return true;
        } else {
            clearInformationText(InfoId);
            return false;
        }
    }

    private boolean checkAllFields() {
        boolean valid = true;

        int passwordInfoId = R.id.passwordInformationText;
        int genderInfoId = R.id.genderInformationText;
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
        if (!isRadioButtonChecked(R.id.maleRadioButton) &&
                !isRadioButtonChecked(R.id.femaleRadioButton)) {
            displayMustEnterField(genderInfoId);
        } else {
            clearInformationText(genderInfoId);
        }
        if (isFieldEmpty(age, ageInfoId)) valid = false;
        if (isFieldEmpty(height, heightInfoId)) valid = false;
        if (isFieldEmpty(weight, weightInfoId)) valid = false;

        return valid;
    }

    private void checkUsername() {
        String username = getUsername();
        if (isUsernameEmpty(username)) return;
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
                            } catch (IOException | JSONException e) {
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

    private Account getAccount() {
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
        Intent intent = MainMenuActivity.getMainMenuIntent(this, username);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void createNewAccount() {
        final String username = getUsername();
        Account account = getAccount();
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
                                    startMainMenuActivity(username);
                                } else {
                                    displayUsernameAlreadyInUse();
                                }
                            } catch (IOException | JSONException e) {
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

    private void setSpinner(Spinner spinner, int stringsArrayId, int selection_index) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                stringsArrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selection_index);
    }

    private void setComponents() {
        this.activitySpinner = (Spinner) findViewById(R.id.activity_spinner);
        this.goalSpinner = (Spinner) findViewById(R.id.goal_spinner);
        this.dietTypeSpinner = (Spinner) findViewById(R.id.diet_type_spinner);

        setSpinner(this.activitySpinner, R.array.activity_spinner_array, 2);
        setSpinner(this.goalSpinner, R.array.goal_spinner_array, 4);
        setSpinner(this.dietTypeSpinner, R.array.diet_type_spinner_array, 1);

        findViewById(R.id.cancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.checkUsernameButton).setOnClickListener(this.checkUsernameButtonClick);
        findViewById(R.id.createButton).setOnClickListener(this.createButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account_layout);
        setComponents();
    }
}
