package com.fitastyclient.data_holders;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CountriesObj implements Serializable {

    @SerializedName("countries")
    private List<String> countries;

    public CountriesObj(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getCountries() {
        return this.countries;
    }

}
