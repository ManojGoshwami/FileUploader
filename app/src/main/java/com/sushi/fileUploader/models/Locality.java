package com.sushi.fileUploader.models;

import com.google.gson.annotations.SerializedName;

public class Locality {
    @SerializedName("Locality_Code")
    private String localityCode;
    @SerializedName("Locality_Name")
    private String localityName;

    public String getLocalityCode() {
        return localityCode;
    }

    public void setLocalityCode(String localityCode) {
        this.localityCode = localityCode;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }
}
