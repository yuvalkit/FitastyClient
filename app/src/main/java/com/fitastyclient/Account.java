package com.fitastyclient;

import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {

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

    public Account(String username, String password, int age, boolean isMale, int height, int weight,
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getAge() {
        return this.age;
    }

    public boolean getIsMale() {
        return this.isMale;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWeight() {
        return this.weight;
    }

    public double getActivityFactor() {
        return this.activityFactor;
    }

    public DietType getDietType() {
        return this.dietType;
    }

    public double getWeightGoal() {
        return this.weightGoal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Account other = (Account) obj;
        return this.username.equals(other.getUsername())
                && this.password.equals(other.getPassword())
                && this.age == other.getAge()
                && this.isMale == other.getIsMale()
                && this.height == other.getHeight()
                && this.weight == other.getWeight()
                && this.activityFactor == other.getActivityFactor()
                && this.dietType.equals(other.getDietType())
                && this.weightGoal == other.getWeightGoal();
    }
}
