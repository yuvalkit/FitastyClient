package com.fitastyclient.data_holders;

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
    @SerializedName("country_name")
    private String country;
    @SerializedName("activity_factor")
    private double activityFactor;
    @SerializedName("diet_type")
    private DietType dietType;
    @SerializedName("weight_goal")
    private double weightGoal;
    @SerializedName("is_vegan")
    private boolean isVegan;
    @SerializedName("is_vegetarian")
    private boolean isVegetarian;
    @SerializedName("is_gluten_free")
    private boolean isGlutenFree;
    @SerializedName("is_lactose_free")
    private boolean isLactoseFree;

    public Account(String username, String password, int age, boolean isMale, int height,
                   int weight, String country, double activityFactor, DietType dietType,
                   double weightGoal, boolean isVegan, boolean isVegetarian, boolean isGlutenFree,
                   boolean isLactoseFree) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.isMale = isMale;
        this.height = height;
        this.weight = weight;
        this.country = country;
        this.activityFactor = activityFactor;
        this.dietType = dietType;
        this.weightGoal = weightGoal;
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isGlutenFree = isGlutenFree;
        this.isLactoseFree = isLactoseFree;
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

    public String getCountry() {
        return this.country;
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

    public boolean getIsVegan() {
        return this.isVegan;
    }

    public boolean getIsVegetarian() {
        return this.isVegetarian;
    }

    public boolean getIsGlutenFree() {
        return this.isGlutenFree;
    }

    public boolean getIsLactoseFree() {
        return this.isLactoseFree;
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
                && this.country.equals(other.getCountry())
                && this.activityFactor == other.getActivityFactor()
                && this.dietType.equals(other.getDietType())
                && this.weightGoal == other.getWeightGoal()
                && this.isVegan == other.getIsVegan()
                && this.isVegetarian == other.getIsVegetarian()
                && this.isGlutenFree == other.getIsGlutenFree()
                && this.isLactoseFree == other.getIsLactoseFree();
    }

    public Sensitivities getSensitivitiesFromAccount() {
        return new Sensitivities(this.isVegan, this.isVegetarian, this.isGlutenFree,
                this.isLactoseFree);
    }
}
