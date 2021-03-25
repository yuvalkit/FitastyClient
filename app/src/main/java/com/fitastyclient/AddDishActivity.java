package com.fitastyclient;

import android.os.Bundle;

public class AddDishActivity extends MyAppCompatActivity {

    private void setComponents() {
        findViewById(R.id.addDishCancelButton).setOnClickListener(this.cancelButtonClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dish_layout);
        setComponents();
    }
}
