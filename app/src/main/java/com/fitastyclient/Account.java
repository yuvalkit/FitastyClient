package com.fitastyclient;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("age")
    private int age;
    @SerializedName("is_male")
    private boolean isMale;
    @SerializedName("height")
    private int height;
    @SerializedName("weight")
    private int weight;
    @SerializedName("activity_factor")
    private double activityFactor;
    @SerializedName("diet_type")
    private DietType dietType;
    @SerializedName("weight_goal")
    private double weightGoal;

    Account(String username, String password, int age, boolean isMale, int height, int weight,
            double activityFactor, DietType dietType, double weightGoal) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.isMale = isMale;
        this.height = height;
        this.weight = weight;
        this.activityFactor = activityFactor;
        this.dietType = dietType;
        this.weightGoal = weightGoal;
    }
}
