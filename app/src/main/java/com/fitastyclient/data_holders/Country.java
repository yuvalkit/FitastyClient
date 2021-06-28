package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Country implements Serializable {

    @SerializedName("country_name")
    private String countryName;

    public Country(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return this.countryName;
    }

}
