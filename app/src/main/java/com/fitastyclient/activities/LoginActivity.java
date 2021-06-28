package com.fitastyclient.activities;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MyAppCompatActivity {

    public static String mustEnterFields = "You must enter username and password.";
    public static String loginFailed = "Login failed, please try again.";
    public static String wrongFields = "Wrong username or password.";

    private View.OnClickListener loginButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearLoginInfoText();
            String username = getTextFromView(R.id.loginLayoutUsernameInput);
            String password = getTextFromView(R.id.loginLayoutPasswordInput);
            tryToLogin(username, password);
        }
    };

    private View.OnClickListener newAccountButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearLoginInfoText();
            tryToCreateEditAccount(LoginActivity.this, true, R.id.newAccountInformationText, null);
        }
    };

    private void startMainMenuActivity(String username) {
        startActivity(Utils.getIntentWithUsername(this, MainMenuActivity.class, username));
    }

    private void tryToLogin(final String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            setLoginInformationText(mustEnterFields);
            return;
        }
        Utils.getRetrofitApi().isCorrectUsernameAndPassword(username, password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                assert response.body() != null;
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                boolean found = jsonObject.getBoolean(Utils.FOUND);
                                if (found) {
                                    startMainMenuActivity(username);
                                } else {
                                    setLoginInformationText(wrongFields);
                                }
                            } catch (Exception e) {
                                setLoginInformationText(loginFailed);
                            }
                        } else {
                            setLoginInformationText(loginFailed);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        setLoginInformationText(loginFailed);
                    }
                });
    }

    private void clearLoginInfoText() {
        clearInformationText(R.id.loginInformationText);
    }

    private void setLoginInformationText(String text) {
        TextView view = findViewById(R.id.loginInformationText);
        view.setTextColor(getResources().getColor(R.color.red));
        view.setText(text);
    }

    private void clearAllTexts() {
        clearInformationText(R.id.loginLayoutUsernameInput);
        clearInformationText(R.id.loginLayoutPasswordInput);
        clearInformationText(R.id.loginInformationText);
    }

    private void setComponents() {
        findViewById(R.id.loginButton).setOnClickListener(this.loginButtonClick);
        findViewById(R.id.newAccountButton).setOnClickListener(this.newAccountButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        setComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearAllTexts();
    }
}
