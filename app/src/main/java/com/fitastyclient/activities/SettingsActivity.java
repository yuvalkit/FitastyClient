package com.fitastyclient.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.fitastyclient.data_holders.Account;
import com.fitastyclient.R;
import com.fitastyclient.Utils;
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
            displayDeleteAccountPopup();
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
        Utils.getRetrofitApi().deleteAccount(username)
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

    private void displayDeleteAccountPopup() {
        displayAlertPopup(deleteTitle, areYouSureText, R.color.red,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                tryToDeleteAccount();
            }
        });
    }

    private void startNewAccountActivity(Account account) {
        Intent intent = getIntentWithBooleanFlag(SettingsActivity.this,
                AccountActivity.class, Utils.IS_CREATE_NEW_ACCOUNT, false);
        intent.putExtra(Utils.ACCOUNT, account);
        startActivity(intent);
    }

    private void tryToEditAccount() {
        Utils.getRetrofitApi().getAccountInformation(this.username)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Account account = Utils.getResponseObject(response, Account.class);
                            if (account != null) {
                                account.setUsername(username);
                                startNewAccountActivity(account);
                                clearEditAccountInformation();
                            } else {
                                displayEditFailed();
                            }
                        } else {
                            displayEditFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
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
