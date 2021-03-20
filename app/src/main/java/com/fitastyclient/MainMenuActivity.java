package com.fitastyclient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {

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

    private View.OnClickListener settingsButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(Utils.getIntentWithUsername(MainMenuActivity.this,
                    SettingsActivity.class, username));
        }
    };

    private View.OnClickListener logOutButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private void setHelloText() {
        String helloText = "Hello " + this.username + "!";
        ((TextView) findViewById(R.id.helloText)).setText(helloText);
    }

    private void setComponents() {
        this.username = Objects.requireNonNull(getIntent().getExtras()).getString(Utils.USERNAME);
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
