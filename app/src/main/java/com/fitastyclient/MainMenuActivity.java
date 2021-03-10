package com.fitastyclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    private View.OnClickListener logOutButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private void setComponents() {
        findViewById(R.id.logOutButton).setOnClickListener(this.logOutButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        setComponents();
    }
}
