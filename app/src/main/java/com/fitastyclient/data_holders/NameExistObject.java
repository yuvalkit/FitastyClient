package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NameExistObject implements Serializable {

    @SerializedName("name_exist")
    private boolean nameExist;

    public NameExistObject(boolean nameExist) {
        this.nameExist = nameExist;
    }

    public boolean getNameExist() {
        return this.nameExist;
    }

}
