package com.sushi.fileUploader;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static SharedPreference pref;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    public static final String NAME = "PdfViewer.pref";
    private SharedPreference(Context ctx) {
        sharedPreference = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }

    public static SharedPreference getInstance(Context ctx) {
        if (pref == null) {
            pref = new SharedPreference(ctx);
        }
        return pref;
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }


    public String getString(String key, String defValue) {
        return sharedPreference.getString(key, defValue);
    }


    public void deleteAllPreference() {
        editor.clear();
        editor.commit();
    }

    public void deletePreference(String key) {
        editor.remove(key);
        editor.commit();
    }

}

