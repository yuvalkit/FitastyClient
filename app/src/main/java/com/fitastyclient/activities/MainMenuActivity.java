package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.DietDiariesObj;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends MyAppCompatActivity {

    public static String hello = "Hello ";
    public static String dietDiariesError = "Failed getting diet diaries, please try again.";

    private String username;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(Utils.FINISH_MAIN_MENU_ACTIVITY)) {
                finish();
            }
        }
    };

    private View.OnClickListener myDietDiariesButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearMainMenuInfoText();
            getDietDiaries();
        }
    };

    private View.OnClickListener addDishButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearMainMenuInfoText();
            startActivity(new Intent(MainMenuActivity.this, AddDishActivity.class));
        }
    };

    private View.OnClickListener addIngredientButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearMainMenuInfoText();
            startActivity(new Intent(MainMenuActivity.this, AddIngredientActivity.class));
        }
    };

    private View.OnClickListener settingsButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearMainMenuInfoText();
            startActivity(Utils.getIntentWithUsername(MainMenuActivity.this,
                    SettingsActivity.class, username));
        }
    };

    private View.OnClickListener logOutButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            clearMainMenuInfoText();
            finish();
        }
    };

    private void displayDietDiariesError() {
        displayError(R.id.mainMenuInfoText, dietDiariesError);
    }

    private void clearMainMenuInfoText() {
        clearInformationText(R.id.mainMenuInfoText);
    }

    private void getDietDiaries() {
        Utils.getRetrofitApi().getDietDiaries(this.username)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            DietDiariesObj dietDiariesObj = Utils.getResponseObject(
                                    response, DietDiariesObj.class);
                            if (dietDiariesObj != null) {
                                Intent intent = Utils.getIntentWithUsername(MainMenuActivity.this,
                                        MyDietDiariesActivity.class, username);
                                intent.putExtra(Utils.DIET_DIARIES_OBJ, dietDiariesObj);
                                startActivity(intent);
                            } else {
                                displayDietDiariesError();
                            }
                        } else {
                            displayDietDiariesError();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayDietDiariesError();
                    }
                });
    }

    private void setHelloText() {
        String helloText = hello + this.username + Utils.EXC_MARK;
        ((TextView) findViewById(R.id.helloText)).setText(helloText);
    }

    private void setComponents() {
//        this.username = Objects.requireNonNull(getIntent().getExtras()).getString(Utils.USERNAME);

        this.username = "123";

        findViewById(R.id.myDietDiariesButton).setOnClickListener(this.myDietDiariesButtonClick);
        findViewById(R.id.addDishButton).setOnClickListener(this.addDishButtonClick);
        findViewById(R.id.addIngredientButton).setOnClickListener(this.addIngredientButtonClick);
        findViewById(R.id.settingsButton).setOnClickListener(this.settingsButtonClick);
        findViewById(R.id.logOutButton).setOnClickListener(this.logOutButtonClick);
        registerReceiver(broadcastReceiver, new IntentFilter(Utils.FINISH_MAIN_MENU_ACTIVITY));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        setComponents();
        setHelloText();
    }
}
