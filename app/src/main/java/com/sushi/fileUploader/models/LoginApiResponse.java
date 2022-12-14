package com.sushi.fileUploader.models;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LoginApiResponse {

    @SerializedName("Code")
    private String code;
    @SerializedName("Message")
    private String message;

    @SerializedName("Designation")
    private String designation;
    @SerializedName("ProfileName")
    private String profileName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}

