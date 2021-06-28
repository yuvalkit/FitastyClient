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

    public static String SERVER_URL = "http://3.142.151.208:8080";

    public static String EMPTY = "";
    public static String SPACE = " ";
    public static String DOT = ".";
    public static String EXC_MARK = "!";
    public static String COLON = ":";
    public static String PERCENT = "%";
    public static String ZERO = "0";
    public static String ALMOST_ZERO = "~0";
    public static String INFINITY = "∞";
    public static String CLOSE_BRACKET = "}";
    public static String CLOSE = "Close";
    public static String CANCEL = "Cancel";
    public static String OK = "OK";
    public static String YES = "Yes";
    public static String NO = "No";
    public static String USERNAME = "username";
    public static String FOUND = "found";
    public static String ACCOUNT = "account";
    public static String SENSITIVITIES = "sensitivities";
    public static String CALORIE_INFO = "calorie_info";
    public static String DIET_DIARY = "diet_diary";
    public static String DIET_DIARIES_OBJ = "diet_diaries_obj";
    public static String COUNTRIES_OBJ = "countries_obj";
    public static String INGREDIENT = "ingredient";
    public static String DISH = "dish";
    public static String MEAL = "meal";
    public static String FACTS_FILTER = "facts_filter";
    public static String MEAL_ID = "meal_id";
    public static String GRAM = "g";
    public static String ML = "ml";
    public static String TEMP_FILLER = "temp_filler";

    public static String IS_CREATE_NEW_ACCOUNT = "is_create_new_account";
    public static String IS_FOR_MEAL = "is_for_meal";
    public static String MEAL_ACTIVITY_TYPE = "meal_activity_type";
    public static String DIET_DIARY_ACTIVITY_TYPE = "diet_diary_activity_type";

    public static String FACTS_PER_100_PREFIX = "Nutrition facts per 100";
    public static String ACTION_FAILED = "Action failed, please try again.";

    public static String FINISH_MAIN_MENU_ACTIVITY = "finish_main_menu_activity";
    public static String DISH_FLAG = "dish_";
    public static String MEAL_FLAG = "meal_";
    public static String ADD_INGREDIENT_TO_TABLE = "add_ingredient_to_table";
    public static String ADD_DISH_TO_TABLE = "add_dish_to_table";
    public static String ITEM_CAN_BE_ADDED_ONCE = "item_can_be_added_once";
    public static String ITEM_ADDED_TO_DISH_CONTENT = "item_added_to_dish_content";
    public static String ITEM_ADD_FAILED = "item_add_failed";
    public static String UPDATE_FACTS_FILTER = "update_facts_filter";
    public static String UPDATE_FACTS_FILTER_BY_ITEM = "update_facts_filter_by_item";
    public static String ADD_MEAL_TO_DIET_DIARY = "add_meal_to_diet_diary";
    public static String EDIT_DIET_DIARY_MEAL = "edit_diet_diary_meal";
    public static String ADD_DIET_DIARY = "add_diet_diary";
    public static String EDIT_DIET_DIARY = "edit_diet_diary";
    public static String PREV_DIET_DIARY_NAME = "prev_diet_diary_name";

    public static double FAT_CALORIE_FACTOR = 9;
    public static double CARB_CALORIE_FACTOR = 4;
    public static double PROTEIN_CALORIE_FACTOR = 4;

    public static double FACTS_FILTER_DEFAULT_MIN_PERCENT = 0.1;
    public static double FACTS_FILTER_DEFAULT_MAX_PERCENT = 10;

    public static double GREEN_THRESHOLD_PERCENT = 0.1;
    public static double GRAY_THRESHOLD_PERCENT = 0.3;

    public static double PERCENT_SCALE = 100;

    public enum ActivityType { CREATE, EDIT, INFO }

    public static void log(String text) {
        Log.d("DEBUG", text);
    }

    public static Intent getIntentWithUsername(Context currActivity, Class<?> newClass,
                                               String username) {
        Intent intent = new Intent(currActivity, newClass);
        Bundle bundle = new Bundle();
        bundle.putString(Utils.USERNAME, username);
        intent.putExtras(bundle);
        return intent;
    }

    public static void displayToast(Context context, String text) {
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
        return s.contains(DOT) ? s.replaceAll("0*$", EMPTY).replaceAll("\\.$", EMPTY) : s;
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
