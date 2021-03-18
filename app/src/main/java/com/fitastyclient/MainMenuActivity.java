package com.fitastyclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {

    static public Intent getMainMenuIntent(Context context, String username) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Utils.USERNAME, username);
        intent.putExtras(bundle);
        return intent;
    }

    private View.OnClickListener logOutButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private void setHelloText() {
        String username = Objects.requireNonNull(getIntent().getExtras())
                .getString(Utils.USERNAME);
        String helloText = "Hello " + username + "!";
        ((TextView) findViewById(R.id.helloText)).setText(helloText);
    }

    private void setComponents() {
        findViewById(R.id.logOutButton).setOnClickListener(this.logOutButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
        setComponents();
        setHelloText();
    }
}
