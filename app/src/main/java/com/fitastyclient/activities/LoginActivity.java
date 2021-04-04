package com.fitastyclient.activities;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.fitastyclient.http.HttpManager;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MyAppCompatActivity {

    static public String mustEnterFields = "You must enter username and password.";
    static public String loginFailed = "Login failed, please try again.";
    static public String wrongFields = "Wrong username or password.";

    private View.OnClickListener loginButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            EditText usernameView = findViewById(R.id.loginLayoutUsernameInput);
            EditText passwordView = findViewById(R.id.loginLayoutPasswordInput);
            String username = usernameView.getText().toString();
            String password = passwordView.getText().toString();
            tryToLogin(username, password);
        }
    };

    private View.OnClickListener newAccountButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, NewEditAccountActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Utils.IS_CREATE_NEW, true);
            intent.putExtras(bundle);
            startActivity(intent);
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
        HttpManager.getRetrofitApi().isCorrectUsernameAndPassword(username, password)
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
                                    setLoginInformationText(Utils.EMPTY);
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

    private void setLoginInformationText(String text) {
        TextView view = findViewById(R.id.loginInformationText);
        view.setTextColor(getResources().getColor(R.color.red));
        view.setText(text);
    }

    private void clearAllTexts() {
        String empty = "";
        ((TextView) findViewById(R.id.loginLayoutUsernameInput)).setText(empty);
        ((TextView) findViewById(R.id.loginLayoutPasswordInput)).setText(empty);
        ((TextView) findViewById(R.id.loginInformationText)).setText(empty);
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
