package com.sushi.fileUploader.models;
import com.google.gson.annotations.SerializedName;

public class ImageApiResponse {
    @SerializedName("Code")
    private String code;
    @SerializedName("Message")
    private String message;

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
}
