package com.fitastyclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    static public String actionFailed = "Action failed, please try again.";

    private String username;

    private View.OnClickListener cancelButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener editAccountButton = new View.OnClickListener() {
        public void onClick(View v) {
            tryToEditAccount();
        }
    };

    private void clearEditAccountInformation() {
        TextView view = findViewById(R.id.editAccountInformationText);
        view.setText(Utils.EMPTY);
    }

    private void displayEditAccountFailed() {
        TextView view = findViewById(R.id.editAccountInformationText);
        view.setTextColor(getResources().getColor(R.color.red));
        view.setText(actionFailed);
    }

    private void startNewAccountActivity(Account account) {
        Intent intent = new Intent(this, NewEditAccountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Utils.IS_CREATE_NEW, false);
        intent.putExtras(bundle);
        intent.putExtra(Utils.ACCOUNT, account);
        startActivity(intent);
    }

    private void tryToEditAccount() {
        HttpManager.getRetrofitApi().getAccountInformation(this.username)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                assert response.body() != null;
                                Account account = (new Gson()).fromJson(
                                        response.body().string(), Account.class);
                                account.setUsername(username);
                                startNewAccountActivity(account);
                                clearEditAccountInformation();
                            } catch (Exception e) {
                                displayEditAccountFailed();
                            }
                        } else {
                            displayEditAccountFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayEditAccountFailed();
                    }
                });
    }

    private void setComponents() {
        this.username = Objects.requireNonNull(getIntent().getExtras()).getString(Utils.USERNAME);
        findViewById(R.id.settingsCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.editAccountButton).setOnClickListener(this.editAccountButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        setComponents();
    }
}
