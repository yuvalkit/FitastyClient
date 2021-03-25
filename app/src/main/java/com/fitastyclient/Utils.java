package com.fitastyclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;

public class Utils {

    static public String EMPTY = "";
    static public String DOT = ".";
    static public String COLON = ":";
    static public String YES = "Yes";
    static public String NO = "No";
    static public String USERNAME = "username";
    static public String FOUND = "found";
    static public String USERNAME_EXIST = "username_exist";
    static public String NAME_EXIST = "name_exist";
    static public String ACCOUNT = "account";
    static public String IS_CREATE_NEW = "is_create_new";
    static public String FINISH_MAIN_MENU_ACTIVITY = "finish_main_menu_activity";
    static public String GRAM = "g";
    static public String ML = "ml";

    static public void log(String text) {
        Log.d("DEBUG", text);
    }

    static public Intent getIntentWithUsername(Context currActivity, Class<?> newClass,
                                               String username) {
        Intent intent = new Intent(currActivity, newClass);
        Bundle bundle = new Bundle();
        bundle.putString(Utils.USERNAME, username);
        intent.putExtras(bundle);
        return intent;
    }

    static public void displayToast(Context context, String text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(0, 0, 0);
        toast.show();
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (K key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }

}
