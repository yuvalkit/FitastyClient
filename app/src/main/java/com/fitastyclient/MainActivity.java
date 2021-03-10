package com.fitastyclient;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

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

    private void tryToLogin(String username, String password) {
        if (checkIfValidUser(username, password)) {
            Utils.log("login succeeded");
        } else {
            Utils.log("login failed");
        }
    }

    private boolean checkIfValidUser(String username, String password) {
        return username.equals("aaa") && password.equals("123");
    }

    private void setComponents() {
        findViewById(R.id.loginButton).setOnClickListener(this.loginButtonClick);
        findViewById(R.id.newAccountButton).setOnClickListener(this.newAccountButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setComponents();

    }

}
