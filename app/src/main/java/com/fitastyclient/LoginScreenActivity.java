package com.fitastyclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreenActivity extends AppCompatActivity {

    private View.OnClickListener loginButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            EditText usernameView = findViewById(R.id.usernameInputBar);
            EditText passwordView = findViewById(R.id.passwordInputBar);
            String username = usernameView.getText().toString();
            String password = passwordView.getText().toString();
            tryToLogin(username, password);
        }
    };

    private View.OnClickListener newAccountButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            Utils.log("create new account");
        }
    };

    private void startMainMenuActivity(String username) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void tryToLogin(final String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Utils.log("please enter username and password");
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
                                String found = jsonObject.getString("found");
                                if (found.equals("True")) {
                                    startMainMenuActivity(username);
                                } else {
                                    Utils.log("wrong username or password");
                                }
                            } catch (IOException | JSONException e) {
                                Utils.log("bad response");
                            }
                        } else {
                            Utils.log("bad response");
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        Utils.log("request failed");
                    }
                });
    }

    private void setComponents() {
        findViewById(R.id.loginButton).setOnClickListener(this.loginButtonClick);
        findViewById(R.id.newAccountButton).setOnClickListener(this.newAccountButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        setComponents();
    }
}
