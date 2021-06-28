package com.fitastyclient.data_holders;

import java.io.Serializable;

public class Sensitivities implements Serializable {

    private boolean isVegan;
    private boolean isVegetarian;
    private boolean isGlutenFree;
    private boolean isLactoseFree;

    public Sensitivities(boolean isVegan, boolean isVegetarian, boolean isGlutenFree,
                         boolean isLactoseFree) {
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isGlutenFree = isGlutenFree;
        this.isLactoseFree = isLactoseFree;
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
}
