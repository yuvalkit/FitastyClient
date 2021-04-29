package com.fitastyclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.fitastyclient.data_holders.NameExistObj;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {

    static public String SERVER_URL = "http://3.142.151.208:8080";

    static public String EMPTY = "";
    static public String SPACE = " ";
    static public String DOT = ".";
    static public String EXC_MARK = "!";
    static public String COLON = ":";
    static public String PERCENT = "%";
    static public String ZERO = "0";
    static public String INFINITY = "∞";
    static public String CLOSE_BRACKET = "}";
    static public String CLOSE = "Close";
    static public String CANCEL = "Cancel";
    static public String OK = "OK";
    static public String YES = "Yes";
    static public String NO = "No";
    static public String USERNAME = "username";
    static public String FOUND = "found";
    static public String ACCOUNT = "account";
    static public String CALORIE_INFO = "calorie_info";
    static public String DIET_DIARY = "diet_diary";
    static public String DIET_DIARIES_OBJ = "diet_diaries_obj";
    static public String INGREDIENT = "ingredient";
    static public String DISH = "dish";
    static public String MEAL = "meal";
    static public String FACTS_FILTER = "facts_filter";
    static public String MEAL_ID = "meal_id";
    static public String GRAM = "g";
    static public String ML = "ml";
    static public String TEMP_FILLER = "temp_filler";

    static public String IS_CREATE_NEW_ACCOUNT = "is_create_new_account";
    static public String IS_FOR_MEAL = "is_for_meal";
    static public String MEAL_ACTIVITY_TYPE = "meal_activity_type";
    static public String DIET_DIARY_ACTIVITY_TYPE = "diet_diary_activity_type";

    static public String FACTS_PER_100_PREFIX = "Nutrition facts per 100";
    static public String ACTION_FAILED = "Action failed, please try again.";

    static public String FINISH_MAIN_MENU_ACTIVITY = "finish_main_menu_activity";
    static public String DISH_FLAG = "dish_";
    static public String MEAL_FLAG = "meal_";
    static public String ADD_INGREDIENT_TO_TABLE = "add_ingredient_to_table";
    static public String ADD_DISH_TO_TABLE = "add_dish_to_table";
    static public String ITEM_CAN_BE_ADDED_ONCE = "item_can_be_added_once";
    static public String ITEM_ADDED_TO_DISH_CONTENT = "item_added_to_dish_content";
    static public String ITEM_ADD_FAILED = "item_add_failed";
    static public String UPDATE_FACTS_FILTER = "update_facts_filter";
    static public String UPDATE_FACTS_FILTER_BY_ITEM = "update_facts_filter_by_item";
    static public String ADD_MEAL_TO_DIET_DIARY = "add_meal_to_diet_diary";
    static public String EDIT_DIET_DIARY_MEAL = "edit_diet_diary_meal";
    static public String ADD_DIET_DIARY = "add_diet_diary";
    static public String EDIT_DIET_DIARY = "edit_diet_diary";
    static public String PREV_DIET_DIARY_NAME = "prev_diet_diary_name";

    static public double FAT_CALORIE_FACTOR = 9;
    static public double CARB_CALORIE_FACTOR = 4;
    static public double PROTEIN_CALORIE_FACTOR = 4;

    static public double FACTS_FILTER_DEFAULT_MIN_PERCENT = 0.1;
    static public double FACTS_FILTER_DEFAULT_MAX_PERCENT = 10;

    static public double CALORIES_THRESHOLD = 100;
    static public double NUTRITION_FACTS_THRESHOLD = 5;

    static public double PERCENT_SCALE = 100;

    public enum ActivityType { CREATE, EDIT, INFO }

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
        TextView v = toast.getView().findViewById(android.R.id.message);
        v.setTextColor(context.getResources().getColor(R.color.white));
        v.setGravity(Gravity.CENTER);
        toast.setGravity(0, 0, 0);
        toast.getView().getBackground().setColorFilter(
                context.getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
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
        if (Math.abs(value) < 0.01) return ZERO;
        String s = String.format(Locale.getDefault(), "%.2f", value);
        return s.contains(".") ? s.replaceAll("0*$","").replaceAll("\\.$","") : s;
    }

    public static double getCalories(double fat, double carb, double protein) {
        return (FAT_CALORIE_FACTOR * fat)
                + (CARB_CALORIE_FACTOR * carb)
                + (PROTEIN_CALORIE_FACTOR * protein);
    }

    public static <T> T getResponseObject(Response<ResponseBody> response, Class<T> objClass) {
        try {
            assert response.body() != null;
            String jsonStr = response.body().string();
            try {
                return getObjectFromJson(jsonStr, objClass);
            } catch (Exception e) {
                try {
                    jsonStr += CLOSE_BRACKET;
                    return getObjectFromJson(jsonStr, objClass);
                } catch (Exception ex) {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getObjectFromJson(String jsonStr, Class<T> objClass) {
        return (new Gson()).fromJson(jsonStr, objClass);
    }

    public static NameExistObj getResponseNameExistObj(Response<ResponseBody> response) {
        return getResponseObject(response, NameExistObj.class);
    }

    public static Api getRetrofitApiByFactory(GsonConverterFactory factory) {
        return new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(factory)
                .build()
                .create(Api.class);
    }

    public static Api getRetrofitApi() {
        return getRetrofitApiByFactory(GsonConverterFactory.create());
    }

    public static Api getRetrofitApiWithNulls() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return getRetrofitApiByFactory(GsonConverterFactory.create(gson));
    }

    public static double roundTwo(double value) {
        int places = 2;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Double zeroMax(Double value) {
        return (value != null) ? Math.max(value, 0) : null;
    }

    public static boolean isEmptyNumber(String num) {
        return num.isEmpty() || num.equals(Utils.DOT);
    }

    public static boolean isValueInThresholdRange(double value, double threshold) {
        return Math.abs(value) < threshold;
    }
}
