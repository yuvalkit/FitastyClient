package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class NameExistObj implements Serializable {

    @SerializedName("name_exist")
    private boolean nameExist;

    public NameExistObj(boolean nameExist) {
        this.nameExist = nameExist;
    }

    public boolean getNameExist() {
        return this.nameExist;
    }
}
