package com.fitastyclient;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fitastyclient.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.log("checking log");
    }
}