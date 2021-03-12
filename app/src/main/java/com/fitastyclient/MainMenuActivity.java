package com.fitastyclient;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {

    private View.OnClickListener logOutButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private void setHelloText() {
        String username = Objects.requireNonNull(getIntent().getExtras()).getString("username");
        String helloText = "Hello " + username + "!";
        ((TextView) findViewById(R.id.helloText)).setText(helloText);
    }

    private void setComponents() {
        findViewById(R.id.logOutButton).setOnClickListener(this.logOutButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        setComponents();
        setHelloText();
    }
}
