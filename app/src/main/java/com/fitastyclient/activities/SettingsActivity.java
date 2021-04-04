package com.fitastyclient.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.fitastyclient.data_holders.Account;
import com.fitastyclient.http.HttpManager;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.google.gson.Gson;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends MyAppCompatActivity {

    static public String deleteTitle = "Delete Account";
    static public String areYouSureText = "Are you sure you want to delete this account?";
    static public String accountDeleted = "Account Deleted";

    private String username;

    private View.OnClickListener editAccountButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            tryToEditAccount();
        }
    };

    private View.OnClickListener deleteAccountButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            displayDeletePopup();
        }
    };

    private void clearEditAccountInformation() {
        TextView view = findViewById(R.id.editAccountInformationText);
        view.setText(Utils.EMPTY);
    }

    private void displayEditFailed() {
        displayActionFailed(R.id.editAccountInformationText);
    }

    private void displayDeleteFailed() {
        displayActionFailed(R.id.deleteAccountInformationText);
    }

    private void tryToDeleteAccount() {
        HttpManager.getRetrofitApi().deleteAccount(username)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            sendBroadcast(new Intent(Utils.FINISH_MAIN_MENU_ACTIVITY));
                            Utils.displayToast(SettingsActivity.this, accountDeleted);
                            finish();
                        } else {
                            displayDeleteFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayDeleteFailed();
                    }
                });
    }

    private void displayDeletePopup() {
        new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setTitle(deleteTitle)
                .setMessage(areYouSureText)
                .setPositiveButton(Utils.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tryToDeleteAccount();
                    }
                })
                .setNegativeButton(Utils.NO, null)
                .setIcon(android.R.drawable.stat_notify_error)
                .show();
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
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(@NonNull Call<Account> call,
                                           @NonNull Response<Account> response) {
                        if (response.isSuccessful()) {
                            Account account = response.body();
                            assert account != null;
                            account.setUsername(username);
                            startNewAccountActivity(account);
                            clearEditAccountInformation();
                        } else {
                            displayEditFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Account> call,
                                          @NonNull Throwable t) {
                        displayEditFailed();
                    }
                });
    }

    private void setComponents() {
        this.username = Objects.requireNonNull(getIntent().getExtras()).getString(Utils.USERNAME);
        findViewById(R.id.settingsCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.editAccountButton).setOnClickListener(this.editAccountButtonClick);
        findViewById(R.id.deleteAccountButton).setOnClickListener(this.deleteAccountButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        setComponents();
    }
}
