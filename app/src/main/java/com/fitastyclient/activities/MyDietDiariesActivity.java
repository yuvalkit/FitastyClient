package com.fitastyclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;

import com.fitastyclient.R;
import com.fitastyclient.Utils;
import com.fitastyclient.data_holders.CalorieInfo;
import com.fitastyclient.data_holders.DietDiary;
import com.fitastyclient.data_holders.DietDiariesObj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDietDiariesActivity extends MyAppCompatActivity {

    public static String deleteTitle = "Delete Diet Diary";
    public static String areYouSureText = "Are you sure you want to delete this diet diary?";

    public static int emptyWidth1 = 30;
    public static int emptyWidth2 = 20;
    public static int dietDiaryTextWidth = 100;
    public static int dietDiaryTextHeight = 150;
    public static int dietDiaryTextWeight = 1;
    public static int dietDiaryTextSize = 18;
    public static int bigLeftPadding = 20;
    public static int smallLeftPadding = 10;
    public static int bigIconSize = 150;
    public static int smallIconSize = 110;

    private String username;
    private List<DietDiary> dietDiaries;
    private TableLayout table;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(Utils.ADD_DIET_DIARY)) {
                DietDiary dietDiary = (DietDiary) intent.getSerializableExtra(Utils.DIET_DIARY);
                dietDiaries.add(dietDiary);
                assert dietDiary != null;
                addDietDiaryToTable(dietDiary);
            } else if (action.equals(Utils.EDIT_DIET_DIARY)) {
                DietDiary dietDiary = (DietDiary) intent.getSerializableExtra(Utils.DIET_DIARY);
                String prevDietDiaryName = Objects.requireNonNull(
                        intent.getExtras()).getString(Utils.PREV_DIET_DIARY_NAME);
                assert dietDiary != null;
                updateDietDiariesContent(prevDietDiaryName, dietDiary.getDietDiaryName());
            }
        }
    };

    private View.OnClickListener addButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            getCalorieInfoForDietDiaryActivity(Utils.ActivityType.CREATE, null);
        }
    };

    private void displayDietDiaryInfoError() {
        displayActionFailed(R.id.myDietDiariesInfoText);
    }

    private void updateDietDiariesContent(String prevDietDiaryName, String newDietDiaryName) {
        deleteDietDiaryFromList(prevDietDiaryName);
        this.dietDiaries.add(new DietDiary(newDietDiaryName));
        this.table.removeAllViews();
        addDietDiariesToTable();
    }

    private void getCalorieInfoForDietDiaryActivity(final Utils.ActivityType activityType,
                                                    final String dietDiaryName) {
        clearInformationText(R.id.myDietDiariesInfoText);
        Utils.getRetrofitApi().getCalorieInfo(this.username)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            CalorieInfo recommendedCalorieInfo = Utils.getResponseObject(
                                    response, CalorieInfo.class);
                            if (recommendedCalorieInfo != null) {
                                if (activityType == Utils.ActivityType.CREATE) {
                                    startDietDiaryActivity(activityType,
                                            recommendedCalorieInfo, null);
                                } else {
                                    getDietDiary(activityType,
                                            recommendedCalorieInfo, dietDiaryName);
                                }
                            } else {
                                displayDietDiaryInfoError();
                            }
                        } else {
                            displayDietDiaryInfoError();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayDietDiaryInfoError();
                    }
                });
    }

    private void getDietDiary(final Utils.ActivityType activityType,
                              final CalorieInfo recommendedCalorieInfo, final String dietDiaryName) {
        Utils.getRetrofitApi().getDietDiary(this.username, dietDiaryName)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            DietDiary dietDiary = Utils.getResponseObject(response,
                                    DietDiary.class);
                            if (dietDiary != null) {
                                startDietDiaryActivity(activityType,
                                        recommendedCalorieInfo, dietDiary);
                            } else {
                                displayDietDiaryInfoError();
                            }
                        } else {
                            displayDietDiaryInfoError();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayDietDiaryInfoError();
                    }
                });
    }

    private void startDietDiaryActivity(Utils.ActivityType activityType,
                                        CalorieInfo recommendedCalorieInfo, DietDiary dietDiary) {
        Intent intent = Utils.getIntentWithUsername(MyDietDiariesActivity.this,
                DietDiaryActivity.class, this.username);
        intent.putExtra(Utils.DIET_DIARY_ACTIVITY_TYPE, activityType);
        intent.putExtra(Utils.CALORIE_INFO, recommendedCalorieInfo);
        if (activityType != Utils.ActivityType.CREATE) {
            intent.putExtra(Utils.DIET_DIARY, dietDiary);
        }
        startActivity(intent);
    }

    private void addDietDiariesToTable() {
        for (DietDiary dietDiary : this.dietDiaries) {
            addDietDiaryToTable(dietDiary);
        }
    }

    private void addDietDiaryToTable(DietDiary dietDiary) {
        TableRow row = new TableRow(this);
        String dietDiaryName = dietDiary.getDietDiaryName();
        addTextViewToRow(row, dietDiaryName, R.color.black, dietDiaryTextWidth,
                dietDiaryTextHeight, dietDiaryTextWeight, bigLeftPadding,
                true, 0, 0, dietDiaryTextSize);
        addInfoButtonToRow(row, dietDiaryName);
        addEmptyTextViewToRow(row, emptyWidth1);
        addEditButtonToRow(row, dietDiaryName);
        addEmptyTextViewToRow(row, emptyWidth2);
        addDeleteButtonToRow(row, dietDiaryName);
        this.table.addView(row);
    }

    private void addInfoButtonToRow(TableRow row, final String dietDiaryName) {
        ImageView view = getImageView(android.R.drawable.ic_dialog_info,
                smallIconSize, R.color.blue, smallLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCalorieInfoForDietDiaryActivity(Utils.ActivityType.INFO, dietDiaryName);
            }
        });
        row.addView(view);
    }

    private void addEditButtonToRow(TableRow row, final String dietDiaryName) {
        ImageView view = getImageView(android.R.drawable.ic_menu_manage,
                bigIconSize, R.color.black, smallLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCalorieInfoForDietDiaryActivity(Utils.ActivityType.EDIT, dietDiaryName);
            }
        });
        row.addView(view);
    }

    private void addDeleteButtonToRow(TableRow row, final String dietDiaryName) {
        ImageView view = getImageView(android.R.drawable.btn_dialog,
                bigIconSize, 0, smallLeftPadding);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInformationText(R.id.myDietDiariesInfoText);
                displayDeleteDietDiaryPopup(view, dietDiaryName);
            }
        });
        row.addView(view);
    }

    private void displayDeleteDietDiaryPopup(final View view, final String dietDiaryName) {
        displayAlertPopup(deleteTitle, areYouSureText, R.color.darkYellow,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendDeleteDietDiaryRequest(dietDiaryName, view);
            }
        });
    }

    private void sendDeleteDietDiaryRequest(final String dietDiaryName, final View view) {
        Utils.getRetrofitApi().deleteDietDiary(username, dietDiaryName)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            deleteItemFromTable(view);
                            deleteDietDiaryFromList(dietDiaryName);
                        } else {
                            displayDietDiaryInfoError();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,
                                          @NonNull Throwable t) {
                        displayDietDiaryInfoError();
                    }
                });
    }

    private void deleteDietDiaryFromList(String dietDiaryName) {
        Iterator<DietDiary> iterator = this.dietDiaries.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getDietDiaryName().equals(dietDiaryName)) {
                iterator.remove();
            }
        }
    }

    private void setComponents() {
        findViewById(R.id.myDietDiariesCancelButton).setOnClickListener(this.cancelButtonClick);
        findViewById(R.id.myDietDiariesAddButton).setOnClickListener(this.addButtonClick);
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.ADD_DIET_DIARY));
        registerReceiver(this.broadcastReceiver, new IntentFilter(Utils.EDIT_DIET_DIARY));
        this.table = findViewById(R.id.myDietDiariesTable);
        this.username = Objects.requireNonNull(getIntent().getExtras()).getString(Utils.USERNAME);
        this.dietDiaries = ((DietDiariesObj) Objects.requireNonNull(
                getIntent().getSerializableExtra(Utils.DIET_DIARIES_OBJ))).getDietDiaries();
        if (!this.dietDiaries.isEmpty()) {
            addDietDiariesToTable();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_diet_diaries_layout);
        setComponents();
    }

}
