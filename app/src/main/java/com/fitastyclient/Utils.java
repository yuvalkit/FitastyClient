package com.fitastyclient;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Utils {

    static public String EMPTY = "";
    static public String USERNAME = "username";
    static public String FOUND = "found";
    static public String USERNAME_EXIST = "username_exist";

    static public void log(String text) {
        Log.d("DEBUG", text);
    }

    static public String hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return String.format("%064x", new BigInteger(1, digest));
        } catch (Exception e) {
            return "";
        }
    }

    static public void displayToast(Context context, String text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(0, 0, 0);
        toast.show();
    }
}
