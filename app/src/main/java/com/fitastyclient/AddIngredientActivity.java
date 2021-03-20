package com.fitastyclient;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AddIngredientActivity extends AppCompatActivity {

    private View.OnClickListener cancelButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private void setComponents() {
        findViewById(R.id.addIngredientCancelButton).setOnClickListener(this.cancelButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredient_layout);
        setComponents();
    }

}
