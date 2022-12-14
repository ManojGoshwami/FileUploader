package com.sushi.fileUploader.models;

public class ImageFileModel {
   private String ID;
   private String UNIQUE_ID;
   private String IMAGE_NAME_PATH;
   private String IMAGE_NAME;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUNIQUE_ID() {
        return UNIQUE_ID;
    }

    public void setUNIQUE_ID(String UNIQUE_ID) {
        this.UNIQUE_ID = UNIQUE_ID;
    }

    public String getIMAGE_NAME_PATH() {
        return IMAGE_NAME_PATH;
    }

    public void setIMAGE_NAME_PATH(String IMAGE_NAME_PATH) {
        this.IMAGE_NAME_PATH = IMAGE_NAME_PATH;
    }

    public String getIMAGE_NAME() {
        return IMAGE_NAME;
    }

    public void setIMAGE_NAME(String IMAGE_NAME) {
        this.IMAGE_NAME = IMAGE_NAME;
    }
}
