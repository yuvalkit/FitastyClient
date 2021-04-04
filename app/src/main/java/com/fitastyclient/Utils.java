package com.fitastyclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.util.Locale;
import java.util.Map;

public class Utils {

    static public String EMPTY = "";
    static public String SPACE = " ";
    static public String DOT = ".";
    static public String COLON = ":";
    static public String PERCENT = "%";
    static public String CLOSE = "Close";
    static public String YES = "Yes";
    static public String NO = "No";
    static public String USERNAME = "username";
    static public String FOUND = "found";
    static public String NAME_EXIST = "name_exist";
    static public String ACCOUNT = "account";
    static public String INGREDIENT = "ingredient";
    static public String DISH = "dish";
    static public String IS_CREATE_NEW = "is_create_new";
    static public String GRAM = "g";
    static public String ML = "ml";

    static public String factsPer100prefix = "Nutrition facts per 100";
    static public String actionFailed = "Action failed, please try again.";

    static public String FINISH_MAIN_MENU_ACTIVITY = "finish_main_menu_activity";
    static public String ADD_INGREDIENT_TO_TABLE = "add_ingredient_to_table";
    static public String ADD_DISH_TO_TABLE = "add_dish_to_table";
    static public String ITEM_CAN_BE_ADDED_ONCE = "item_can_be_added_once";
    static public String ITEM_ADDED_TO_DISH_CONTENT = "item_added_to_dish_content";

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
        toast.getView().getBackground().setColorFilter(
                context.getResources().getColor(R.color.mildBlue), PorterDuff.Mode.SRC_IN);
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

    public static String doubleToPercent(double value) {
        return ((int) (value * 100)) + PERCENT;
    }

    public static String cleanDoubleToString(double value) {
        String s = String.format(Locale.getDefault(), "%.2f", value);
        return s.contains(".") ? s.replaceAll("0*$","").replaceAll("\\.$","") : s;
    }
}
